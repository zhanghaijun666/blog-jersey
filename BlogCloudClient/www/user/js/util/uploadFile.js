(function () {
    setTimeout(function () {
        var fileToUpload = document.getElementById("fileToUpload");

        fileToUpload.addEventListener("change", function (evt) {
            evt.preventDefault();
            console.log("---------------");
            console.log(fileToUpload);
            console.log("---------------");
        });

    }, 1000);
})();