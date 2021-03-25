function inputPhoneNumber(obj) {
    let pattern =  /[ㄱ-ㅎ|ㅏ-ㅓ|가-힣|a-z|A-Z]/g;

    if(pattern.test(obj.value)){
        obj.value="";
    }else {
        let number = obj.value.replace(/[^0-9]/g, "");
        let phone = "";
        if (number.length < 4) {
            return number;
        } else if (number.length < 7) {
            phone += number.substr(0, 3);
            phone += "-";
            phone += number.substr(3);
        } else if (number.length < 11) {
            phone += number.substr(0, 3);
            phone += "-";
            phone += number.substr(3, 3);
            phone += "-";
            phone += number.substr(6);
        } else {
            phone += number.substr(0, 3);
            phone += "-";
            phone += number.substr(3, 4);
            phone += "-";
            phone += number.substr(7);
        }
        obj.value = phone;
    }
}

function check_pw() {
    let pw = document.getElementById('accountPw');
    let pwConfirm = document.getElementById('passwordConfirm');
    let check = document.getElementById('check');

    if(pw.value !=='' && pwConfirm.value !=='') {
        if(pw.value === pwConfirm.value) {
            check.innerText = '비밀번호가 일치합니다.';
            check.style.color='blue';
            pwConfirm.value = "true";
            pwConfirm.value = pw.value;
            return;
        }
        check.innerText = '비밀번호가 일치하지 않습니다.'
        check.style.color = 'red';
    }
}

function checkUserIdExist() {
    let user_id = $("#accountId").val();
    if(user_id.length === 0) {
        alert('아이디를 작성해주세요')
        return
    }
    $.ajax({
        url : '/member/checkIdExist/' + user_id,
        type : 'get',
        data : null,
        dataType : 'text',
        success : function(result) {
            if(result.trim()=='true') {
                alert('사용할 수 없는 아이디 입니다.');
                $("#duplicationCheck").val('false');

            }else {
                alert('사용할 수 있는 아이디 입니다');
                $("#duplicationCheck").val('true');
            }
        }
    });
}

function resultSubmit() {
    let check = document.getElementById("duplicationCheck");
    let joinForm = document.getElementById("joinForm");
    let pw = document.getElementById('accountPw');
    let pwConfirm = document.getElementById('passwordConfirm');

    let name = document.getElementById('name').value;
    let email = document.getElementById('email').value;
    let phoneNumber = document.getElementById('phoneNumber').value;

    if(check.value !== 'true' && pwConfirm.value !== 'true'){
        alert('아이디, 비밀번호를 입력해주세요')
        return;
    }

    if(name !== '' && email !=='' && phoneNumber !== '') {
        if(check.value === 'true' && (pw.value ===pwConfirm.value)) {
            return joinForm.submit();
        }

        if(check.value !== 'true') {
            alert("아이디 증복검사를 해야합니다.");
        }else {
            alert("비밀번호가 일치하지 않습니다.");
        }

    }else {
        alert('필수정보 이름, 이메일, 핸드폰번호를 입력해주세요.' )
    }
}