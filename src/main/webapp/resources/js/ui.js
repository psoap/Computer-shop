function changeLang() {
    document.getElementById('lang-form').submit();
}

function editCategoryLang() {
    let select = document.getElementById('select_id');
    document.getElementById('lang_code').value = select.options[select.selectedIndex].className;
}