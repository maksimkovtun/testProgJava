document.getElementById('upload-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData();
    const fileInput = document.getElementById('file-input');
    if (fileInput.files.length > 0) {
        formData.append("file", fileInput.files[0]);
        fetch('/api/upload-zip', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                alert('Файл успешно загружен');
            })
            .catch(error => {
                console.error('Ошибка:', error);
                alert('Ошибка загрузки файла');
            });
    } else {
        alert('Выберите файл!');
    }
});
