<!-- 이미지 미리 보여주기 -->
function readURL(input) {
    if (input.files && input.files[0]) {
        //파일이름 표시
        let imgLabel = document.getElementById("imgLabel");

        imgLabel.innerHTML = input.files[0].name;

        //이미지 교체
        let imgParent = document.getElementById("imgParent");
        if (imgParent.length != 0) {
            while (imgParent.hasChildNodes()) {
                imgParent.removeChild(imgParent.firstChild);
            }
        }

        //이미지 미리보기
        const reader = new FileReader();

        reader.onload = function (e) {

            let preImg = document.createElement("img");
            preImg.setAttribute("src", e.target.result);
            preImg.style.width = "500px";
            preImg.style.height = "500px";
            imgParent.appendChild(preImg);

        };

        reader.readAsDataURL(input.files[0]);
    }
}