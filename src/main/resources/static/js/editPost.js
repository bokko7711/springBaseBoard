const path = window.location.pathname; // "/post/6"
const segments = path.split("/"); // ["", "post", "6"]
const postId = segments.pop(); // 마지막 요소 가져오기

// 게시글 데이터 불러오기
fetch(`/api/post/${postId}`)
    .then(response => response.json())
    .then(post => {
        let attachFileHTML = "";
        if (post.file) {
            attachFileHTML = `<a href="/api/attach/${post.file.id}">
        기존파일 : ${post.file.uploadFileName}</a>`;
        } else {
            attachFileHTML = '기존파일 : 선택된 파일 없음';
        }
        document.getElementById("edit_form").innerHTML = `
      <form action="/post/edit/${postId}" method="post" enctype="multipart/form-data">
        <label for="title">제목 : </label>
        <input type="text" id="title" name="title" value="${post.title}" placeholder="제목 입력">
        <label for="attachFile">첨부파일 : </label>
        <input type="file" id="attachFile" name="attachFile">
        <span id="fileName">${attachFileHTML}</span>
        <label for="content">본문 : </label>
        <textarea id="content" name="content" placeholder="내용 입력">${post.content}</textarea>
        
        <button type="submit">수정</button>
      </form>
    `;
    document.getElementById("attachFile").addEventListener("change", function () {
        const newFileName = this.files[0] ? this.files[0].name : "선택된 파일 없음";
        document.getElementById("fileName").textContent = newFileName;
    });
    })
    .catch(error => {
        console.error("게시글 불러오기 실패:", error);
        document.getElementById("edit_form").innerHTML = "<p>게시글을 불러올 수 없습니다.</p>";
    });