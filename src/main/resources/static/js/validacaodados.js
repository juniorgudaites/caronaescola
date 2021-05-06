function enviardados() {
    if(document.getElementById(nome).value==""){
        alert("Campo Nome não pode vazio");
        document.dados.nome.focus();
        return false;
    }
}