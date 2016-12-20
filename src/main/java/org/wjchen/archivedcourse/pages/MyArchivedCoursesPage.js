$(document).ready(function() {
	$('.annual-pack-panel').each(function(index) {
		if(index > 0) {
			$(this).find('.panel-body').slideUp();
			$(this).find('.panel-heading').addClass('panel-collapsed');
		}
	});
})

$(document).on('click', '.panel-heading', function(e){
    var $this = $(this);
	if(!$this.hasClass('panel-collapsed')) {
		$this.parents('.annual-pack-panel').find('.panel-body').slideUp();
		$this.addClass('panel-collapsed');
	} else {
		$this.parents('.annual-pack-panel').find('.panel-body').slideDown();
		$this.removeClass('panel-collapsed');
	}
})
