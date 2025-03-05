package com.program.testProgJava.rest;

import com.program.testProgJava.dao.entities.*;
import com.program.testProgJava.dao.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;
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
            try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(tempFile))) {
                ZipEntry entry;
                Map<String, List<Object>> entitiesMap = new HashMap<>();
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (entry.getName().endsWith(".csv")) {
                        System.out.println("Processing CSV: " + entry.getName());
                        processCsv(zipInputStream, entry.getName(), entitiesMap);
                    }
                }
                saveEntities(entitiesMap);
            }
            return ResponseEntity.ok("Архив успешно загружен и обработан.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при загрузке архива.");
        }
    }

    private void processCsv(ZipInputStream zipInputStream, String fileName, Map<String, List<Object>> entitiesMap) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (fileName.equalsIgnoreCase("PositionType.csv")) {
                    String[] fields = line.split(";");
                    PositionsEntity position = new PositionsEntity(fields[0], fields[1]);
                    entitiesMap.computeIfAbsent("positions", k -> new ArrayList<>()).add(position);
                } else if (fileName.equalsIgnoreCase("Shop.csv")) {
                    String[] fields = line.split(";");
                    StoresEntity store = new StoresEntity(fields[0], fields[1]);
                    entitiesMap.computeIfAbsent("stores", k -> new ArrayList<>()).add(store);
                } else if (fileName.equalsIgnoreCase("ElectroType.csv")) {
                    String[] fields = line.split(";");
                    ElectronicsTypesEntity electronicsTypes = new ElectronicsTypesEntity(fields[0], fields[1]);
                    entitiesMap.computeIfAbsent("electronicsTypes", k -> new ArrayList<>()).add(electronicsTypes);
                } else if (fileName.equalsIgnoreCase("PurchaseType.csv")) {
                    String[] fields = line.split(";");
                    PurchaseTypesEntity purchaseType = new PurchaseTypesEntity(fields[0], fields[1]);
                    entitiesMap.computeIfAbsent("purchaseTypes", k -> new ArrayList<>()).add(purchaseType);
                } else if (fileName.equalsIgnoreCase("Employee.csv")) {
                    String[] fields = line.split(";");
                    EmployeesEntity employee = new EmployeesEntity(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7]);
                    entitiesMap.computeIfAbsent("employees", k -> new ArrayList<>()).add(employee);
                } else if (fileName.equalsIgnoreCase("ElectroItem.csv")) {
                    String[] fields = line.split(";");
                    ElectronicsProductsEntity electronicsProducts = new ElectronicsProductsEntity(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6]);
                    entitiesMap.computeIfAbsent("electronicsProducts", k -> new ArrayList<>()).add(electronicsProducts);
                } else if (fileName.equalsIgnoreCase("Purchase.csv")) {
                    String[] fields = line.split(";");
                    PurchasesEntity purchase = new PurchasesEntity(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
                    entitiesMap.computeIfAbsent("purchases", k -> new ArrayList<>()).add(purchase);
                } else if (fileName.equalsIgnoreCase("ElectroEmployee.csv")) {
                    String[] fields = line.split(";");
                    EmployeeElectronicsTypeLinkEntity eetLink = new EmployeeElectronicsTypeLinkEntity(fields[0], fields[1]);
                    entitiesMap.computeIfAbsent("eetLinks", k -> new ArrayList<>()).add(eetLink);
                } else if (fileName.equalsIgnoreCase("ElectroShop.csv")) {
                    String[] fields = line.split(";");
                    ElectronicsProductStoreLinkEntity epsLink = new ElectronicsProductStoreLinkEntity(fields[0], fields[1], fields[2]);
                    entitiesMap.computeIfAbsent("epsLinks", k -> new ArrayList<>()).add(epsLink);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обработке CSV-файла: " + fileName);
        }
    }

    private void saveEntities(Map<String, List<Object>> entitiesMap) {
        if (entitiesMap.containsKey("positions")) {
            List<PositionsEntity> positions = (List<PositionsEntity>) (List<?>) entitiesMap.get("positions");
            positionsRepository.saveAll(positions);
        }

        if (entitiesMap.containsKey("stores")) {
            List<StoresEntity> stores = (List<StoresEntity>) (List<?>) entitiesMap.get("stores");
            storeRepository.saveAll(stores);
        }

        if (entitiesMap.containsKey("electronicsTypes")) {
            List<ElectronicsTypesEntity> electronicsTypes = (List<ElectronicsTypesEntity>) (List<?>) entitiesMap.get("electronicsTypes");
            electronicsTypesRepository.saveAll(electronicsTypes);
        }

        if (entitiesMap.containsKey("purchaseTypes")) {
            List<PurchaseTypesEntity> purchaseTypes = (List<PurchaseTypesEntity>) (List<?>) entitiesMap.get("purchaseTypes");
            purchaseTypesRepository.saveAll(purchaseTypes);
        }

        if (entitiesMap.containsKey("employees")) {
            List<EmployeesEntity> employees = (List<EmployeesEntity>) (List<?>) entitiesMap.get("employees");
            employeesRepository.saveAll(employees);
        }

        if (entitiesMap.containsKey("electronicsProducts")) {
            List<ElectronicsProductsEntity> electronicsProducts = (List<ElectronicsProductsEntity>) (List<?>) entitiesMap.get("electronicsProducts");
            electronicsProductsRepository.saveAll(electronicsProducts);
        }

        if (entitiesMap.containsKey("purchases")) {
            List<PurchasesEntity> purchases = (List<PurchasesEntity>) (List<?>) entitiesMap.get("purchases");
            purchasesRepository.saveAll(purchases);
        }

        if (entitiesMap.containsKey("eetLinks")) {
            List<EmployeeElectronicsTypeLinkEntity> eetLinks = (List<EmployeeElectronicsTypeLinkEntity>) (List<?>) entitiesMap.get("eetLinks");
            eetLinkRepository.saveAll(eetLinks);
        }

        if (entitiesMap.containsKey("epsLinks")) {
            List<ElectronicsProductStoreLinkEntity> epsLinks = (List<ElectronicsProductStoreLinkEntity>) (List<?>) entitiesMap.get("epsLinks");
            epsLinkRepository.saveAll(epsLinks);
        }
    }
}
