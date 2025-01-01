function playPause(button) {
	let audio = button.audio;
	const audioSrc = button.getAttribute("data-audio-src");
	console.log("Button clicked:", button);
    console.log("Audio source:", button.getAttribute("data-audio-src"));  // デバッグ用

	
	if (!audio) {
		audio = new Audio(audioSrc);
		button.audio = audio;
		audio.onended = () => {
			button.textContent = "再生";
		};
	}
	if (audio.paused) {
		audio.play();
		button.textContent = "停止";
	} else {
		audio.pause();
		button.textContent = "再生";
	}
}

function limitPlayTime(audio, maxTime) {
        if (audio.currentTime > maxTime) {
            audio.pause();
            audio.currentTime = 0;
            alert("フルバージョンをお楽しみいただくには、有料会員にご登録ください。");
        }
}