$(document).ready(function() {
	var userAgent = navigator.userAgent.toLowerCase();
	//console.log(userAgent);

	$('textarea').each(function() {
		var $this = $(this);
		$(this).css("width", '100%');

		var rawXml = $this.text().trim();
		$this.val(rawXml);
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