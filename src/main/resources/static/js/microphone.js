const btnStartRecord = document.getElementById('btnStartRecord');
const btnStopRecord = document.getElementById('btnStopRecord');
const descripcion = document.getElementById('descripcion');

let recognition = new webkitSpeechRecognition();
recognition.lang = 'es-ES';
recognition.continuous = true;
recognition.interimResults = false;

recognition.onresult = (event) =>{
    const results = event.results;
    const frase = results[results.length-1][0].transcript;
    descripcion.value += frase;
}

recognition.onend = (event) => {
    console.log('El microfono dejo de escuchar');
}

recognition.onerror = (event) => {
    console.log(event.error);
}

btnStartRecord.addEventListener('click',() =>{
    recognition.start();
});

btnStopRecord.addEventListener('click',() =>{
    recognition.abort();
});

