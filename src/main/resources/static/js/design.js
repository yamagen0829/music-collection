$(document).ready(function () {
	$('.carousel').slick({
		autoplay: true,
		dots: true,
		infinite: true,
		speed: 500,
		fade: true,
		cssEase: 'ease',
		autoplaySpeed: 2000,
		arrows: true,
	});
	
	$(window).on('scroll', function(){
			$('.js-fade').each(function(){
			const pos = $(this).offset().top;
			const scroll = $(window).scrollTop();
			const windowHeight = $(window).height();
			if (scroll > pos - windowHeight + 100) {
				$(this).stop().addClass('scroll');
			} else {
				$(this).stop().removeClass('scroll');
			}
		});	
	});
	
	$(window).on('scroll',function(){
		const backbtn = document.getElementById('backbtn');
	    const scrollValue = document.scrollingElement.scrollTop;
			
			if (scrollValue >= 300) {
				backbtn.style.display = 'inline';
			} else {
				backbtn.style.display = 'none';
			}
	}); 
	
	$('#backbtn').on('click', function(event){
		event.preventDefault();
		$('html, body').animate({ scrollTop: 0}, '300');
	});
});