package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.*;
import com.program.testProgJava.dao.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.*;

@RestController
@RequestMapping("/api")
public class ImportController {
    private final StoresRepository storeRepository;
    private final ElectronicsProductStoreLinkRepository epsLinkRepository;
    private final EmployeeElectronicsTypeLinkRepository eetLinkRepository;
    private final ElectronicsProductsRepository electronicsProductsRepository;
    private final ElectronicsTypesRepository electronicsTypesRepository;
    private final EmployeesRepository employeesRepository;
    private final PositionsRepository positionsRepository;
    private final PurchasesRepository purchasesRepository;
    private final PurchaseTypesRepository purchaseTypesRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public ImportController(StoresRepository storeRepository, ElectronicsProductStoreLinkRepository epsLinkRepository, EmployeeElectronicsTypeLinkRepository eetLinkRepository, ElectronicsProductsRepository electronicsProductsRepository, ElectronicsTypesRepository electronicsTypesRepository, EmployeesRepository employeesRepository, PositionsRepository positionsRepository, PurchasesRepository purchasesRepository, PurchaseTypesRepository purchaseTypesRepository) {
        this.storeRepository = storeRepository;
        this.epsLinkRepository = epsLinkRepository;
        this.eetLinkRepository = eetLinkRepository;
        this.electronicsProductsRepository = electronicsProductsRepository;
        this.electronicsTypesRepository = electronicsTypesRepository;
        this.employeesRepository = employeesRepository;
        this.positionsRepository = positionsRepository;
        this.purchasesRepository = purchasesRepository;
        this.purchaseTypesRepository = purchaseTypesRepository;
    }

    @PostMapping("/upload-zip")
    public ResponseEntity<String> uploadZipFile(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("uploaded", ".zip");
            file.transferTo(tempFile);

            try (ZipFile zipFile = new ZipFile(tempFile)) {
                Map<String, List<Object>> entitiesMap = new ConcurrentHashMap<>();
                List<Future<?>> futures = new ArrayList<>();

                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".csv")) {
                        futures.add(executorService.submit(() -> processCsv(zipFile, entry, entitiesMap)));
                    }
                }

                for (Future<?> future : futures) {
                    future.get();
                }

                saveEntities(entitiesMap);
            }
            String jsonResponse = "{\"message\": \"Архив успешно загружен\"}";
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при загрузке архива.");
        }
    }

    private java.sql.Date parseDate(String dateString) {
        try {
            if (dateString == null || dateString.trim().isEmpty()) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date utilDate = sdf.parse(dateString.trim());
            return new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateString, e);
        }
    }

    private void processCsv(ZipFile zipFile, ZipEntry entry, Map<String, List<Object>> entitiesMap) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(zipFile.getInputStream(entry), "Windows-1251"))) {

            String fileName = entry.getName();
            String headerLine = reader.readLine();
            if (headerLine == null) return;

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = splitCsvLine(line);

                if (columns.length > 1) {
                    columns[1] = columns[1].replace("“", "").replace("”", "");
                }

                switch (fileName) {
                    case "PositionType.csv":
                        entitiesMap.computeIfAbsent("positions", k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(new PositionsEntity(columns[0], columns[1]));
                        break;
                    case "Shop.csv":
                        entitiesMap.computeIfAbsent("stores", k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(new StoresEntity(columns[0], columns[1], columns[2]));
                        break;
                    case "ElectroType.csv":
                        entitiesMap.computeIfAbsent("electronicsTypes", k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(new ElectronicsTypesEntity(columns[0], columns[1]));
                        break;
                    case "PurchaseType.csv":
                        entitiesMap.computeIfAbsent("purchaseTypes", k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(new PurchaseTypesEntity(columns[0], columns[1]));
                        break;
                    case "Employee.csv":
                        entitiesMap.computeIfAbsent("employees", k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(new EmployeesEntity(
                                        columns[0], columns[1], columns[2], columns[3],
                                        parseDate(columns[4]), columns[5], columns[6], columns[7]
                                ));
                        break;
                    case "ElectroItem.csv":
                        entitiesMap.computeIfAbsent("electronicsProducts", k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(new ElectronicsProductsEntity(
                                        columns[0], columns[1], columns[2],
                                        columns[3], columns[4], columns[5], columns[6]
                                ));
                        break;
                    case "ElectroShop.csv":
                        entitiesMap.computeIfAbsent("epsLinks", k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(new ElectronicsProductStoreLinkEntity(columns[0], columns[1], columns[2]));
                        break;
                    case "ElectroEmployee.csv":
                        entitiesMap.computeIfAbsent("eetLinks", k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(new EmployeeElectronicsTypeLinkEntity(columns[0], columns[1]));
                        break;
                    case "Purchase.csv":
                        entitiesMap.computeIfAbsent("purchases", k -> Collections.synchronizedList(new ArrayList<>()))
                                .add(new PurchasesEntity(columns[0], columns[1], columns[2], parseDate(columns[3]), columns[4], columns[5]));
                        break;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обработке CSV-файла: " + entry.getName());
        }
    }

    private String[] splitCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ';' && !inQuotes) {
                result.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString().trim());
        return result.toArray(new String[0]);
    }

    private void saveEntities(Map<String, List<Object>> entitiesMap) {
        positionsRepository.saveAll((List<PositionsEntity>) (List<?>) entitiesMap.getOrDefault("positions", Collections.emptyList()));
        storeRepository.saveAll((List<StoresEntity>) (List<?>) entitiesMap.getOrDefault("stores", Collections.emptyList()));
        electronicsTypesRepository.saveAll((List<ElectronicsTypesEntity>) (List<?>) entitiesMap.getOrDefault("electronicsTypes", Collections.emptyList()));
        purchaseTypesRepository.saveAll((List<PurchaseTypesEntity>) (List<?>) entitiesMap.getOrDefault("purchaseTypes", Collections.emptyList()));
        employeesRepository.saveAll((List<EmployeesEntity>) (List<?>) entitiesMap.getOrDefault("employees", Collections.emptyList()));
        electronicsProductsRepository.saveAll((List<ElectronicsProductsEntity>) (List<?>) entitiesMap.getOrDefault("electronicsProducts", Collections.emptyList()));
        epsLinkRepository.saveAll((List<ElectronicsProductStoreLinkEntity>) (List<?>) entitiesMap.getOrDefault("epsLinks", Collections.emptyList()));
        eetLinkRepository.saveAll((List<EmployeeElectronicsTypeLinkEntity>) (List<?>) entitiesMap.getOrDefault("eetLinks", Collections.emptyList()));
        purchasesRepository.saveAll((List<PurchasesEntity>) (List<?>) entitiesMap.getOrDefault("purchases", Collections.emptyList()));
    }

}
