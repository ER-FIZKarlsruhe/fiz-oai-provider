$(document).ready(function() {
	var userAgent = navigator.userAgent.toLowerCase();
	//console.log(userAgent);

	$('textarea').each(function() {
		$(this).css("width", '100%');

		if (userAgent.includes("firefox")) {
			var md = $(this).html();
			$(this).text(md);
		}

		var ta = $(this);
		ta.css("display", 'none');
		autosize(ta);
		ta.css("display", 'block');

		// Call the update method to recalculate the size:
		autosize.update(ta);
	});

});