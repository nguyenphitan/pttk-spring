
$(document).ready(function() {
	//admin bill button complete
	const btnCompletes = document.querySelectorAll('.btn_complete')
	console.log(btnCompletes.length)
	
	btnCompletes.forEach((btnComplete) => {
		let text = btnComplete.innerText;
		if( text == "Đã hoàn thành") {
		    btnComplete.classList.add('complete');
		}
	})
	
	
	/*
		Kiểm tra role:
		1. Nếu là SELLER hoặc ADMIN thì làm mờ nút thêm nhân viên
		2. Ngược lại, hiển thị nút thêm nhân viên
		
		Created by: NPTAN (24/04/2022)
		Version: 1.0
	*/
	let roles = $('.account_role');
	console.log(roles);
	for(let role of roles) {
		let roleValue = $(role).text();
		if(roleValue == 'SELLER' || roleValue == 'ADMIN') {
			$(role).prev().addClass('complete');
			$(role).prev().text("Đã thêm");
		}
	}
	
	
	// Click thêm nhân viên:
	$('.add_seller').click(function(e) {
		// 1. Lấy ra id của account:
		let id = Number($(e.target).prev().text());
		
		// 2. Gọi đến API thêm seller:
		$.ajax({
            type: "POST",
            url: "http://localhost:8088/admin/seller",
            // async: false,
            data: JSON.stringify(id),
            dataType: "json",
            contentType: "application/json",
            success: function (response) {
            	alert("Thêm thành công.");
            	window.location.reload();
            },
            error: function(reject) {
                alert("Không thành công.");
                console.log(reject);
            }
        });	
		
	})
	
	
	// Click xóa nhân viên (chỉ xóa role, không xóa tài khoản):
	$('.remove_seller').click(function() {
		// 1. Lấy ra id của account:
		let id = Number($(e.target).prev().text());
		
		// 2. Gọi đến API xóa seller:
		$.ajax({
            type: "DELETE",
            url: `http://localhost:8088/admin/seller/${id}`,
            success: function (response) {
                alert("Xóa thành công.");
                window.location.reload();
            }
        });
        
	})
	
	
	
})