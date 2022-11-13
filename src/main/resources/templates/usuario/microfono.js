const grabar = document.getElementById("grabar");
const texto = document.getElementById("descripcion");

let recognition = new webkitSpeechRecognition();
recognition.lang = 'es-ES';
recognition.continuous = true;
recognition.interimResults = false;
recognition.onresult = event => {
    for(const result of event.result){
        console.log(result[0].transcript)
    }
}
grabar.addEventListener('click',()=>{
    recognition.start();
});
