function deleteSomething( ) {
	message = 'คุณต้องการลบข้อมูลรายการนี้ใช่หรือไม่?';
	if ( confirm(message) ) {
		return true;
	} else {
		return false;
	}
}

function convertSomething( ) {
	message = 'ยืนยันการเปลียนแปลงข้อมูล?';
	if ( confirm(message) ) {
		return true;
	} else {
		return false;
	}
}

function confirm_logout( ) {
	message = 'คุณต้องการออกจากระบบใช่หรือไม่?';
	if ( confirm(message) ) {
		return true;
	} else {
		return false;
	}
}

function getURLParameter(name) {
    return decodeURI(
        (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
    );
}


