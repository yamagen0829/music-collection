const musicInput = document.getElementById('musicFile');
const musicPreview = document.getElementById('musicPreview');
 
 musicInput.addEventListener('change', () => {
   if (musicInput.files[0]) {
     let fileReader = new FileReader();
     fileReader.onload = () => {
       musicPreview.innerHTML = `<audio id="audioPlayer" src="${fileReader.result}" controls></audio>`;
     };
     fileReader.readAsDataURL(musicInput.files[0]);
   } else {
     musicPreview.innerHTML = '';
   }
});   