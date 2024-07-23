
document.getElementById("toggleButton").addEventListener("click", function() {
    var sidebar = document.querySelector('.sidebar');
    var content = document.querySelector('.content');
    var head=document.getElementById('header')
    if (sidebar.style.left === "-170px") {
        sidebar.style.left = "0";
        content.style.marginLeft = "200px";

    } else {
        sidebar.style.left = "-170px";
        content.style.marginLeft = "30px";

    }
});



