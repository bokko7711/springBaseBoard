document.addEventListener("DOMContentLoaded", function () {
    fetch("/api/profile")
        .then(response => response.json())
        .then(data => {
            document.getElementById("profile").innerHTML = `
                <p>아이디 : ${data.loginId}</p>
                <p>비밀번호 : ${data.password}</p>
                <p>이름 : ${data.name}</p>
                <p>나이 : ${data.age}</p>
            `;
        })
        .catch(error => console.error("Error fetching profile:", error));
});