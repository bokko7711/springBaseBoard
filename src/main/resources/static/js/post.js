const path = window.location.pathname; // "/post/6"
const segments = path.split("/"); // ["", "post", "6"]
const postId = segments.pop(); // 마지막 요소 가져오기

document.addEventListener("DOMContentLoaded", function () {
    Promise.all([
        fetch(`/api/post/${postId}`).then(response => response.json()),
        fetch("/api/profile").then(response => response.json()),
        fetch(`/api/comments/${postId}`).then(response => response.json())
    ])
        .then(([post, profile, comments]) => {
            paintPost(profile, post);
            paintComments(profile, comments);
            paintAddComment(profile);
        })
        .catch(error => console.error("Error fetching data:", error));
});

const paintPost = (profile, post) => {
    let attachFileHTML = "";
    if (post.file) {
        attachFileHTML = `첨부파일: <a href="/api/attach/${post.file.id}">
        ${post.file.uploadFileName}</a>`;
    }
    document.getElementById("post").innerHTML = `
        <h3>${post.title}</h3>
        <span>작성자 : ${post.author}</span></br>
        <span>${attachFileHTML}</span>
        <p>${post.content}</p>
    `;
    if (profile.name == post.author) {
        document.getElementById("update_and_delete").innerHTML = `
            <button onclick="window.location.href='/post/edit/${postId}'">수정</button>
            <form action="/post/delete/${postId}" method="post">
                <button type="submit">삭제</button>
            </form>
        `;
    }
};

const paintComments = (profile, comments) => {
    const commentList = document.getElementById("comments");
    commentList.innerHTML = ""; // 기존 리스트 초기화

    comments.forEach(comment => {
        const li = document.createElement("li");

        // 댓글 내용 표시 (기본은 span 태그)
        const span = document.createElement("span");
        span.textContent = `${comment.author} : ${comment.content}`;

        // 수정 버튼
        const editButton = document.createElement("button");
        editButton.textContent = "수정";

        // 삭제 버튼 (작성자와 같을 경우만 표시)
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "삭제";
        deleteButton.onclick = () => deleteComment(profile, comment.id);

        // 수정 기능
        editButton.onclick = () => {
            const input = document.createElement("input");
            input.type = "text";
            input.value = comment.content; // 기존 댓글 내용을 input에 넣음

            const submitButton = document.createElement("button");
            submitButton.textContent = "저장";

            // 저장 버튼 클릭 시 새로운 내용 적용
            submitButton.onclick = () => {
                const newContent = input.value.trim();
                if (!newContent) {
                    alert("내용을 입력하세요!");
                    return;
                }

                // 서버에 댓글 업데이트 요청 (여기서는 가정)
                updateComment(comment.id, newContent);

                // UI 업데이트 (input → span으로 변경)
                span.textContent = `${comment.author} : ${newContent}`;
                li.replaceChild(span, input);
                li.replaceChild(editButton, submitButton);
            };

            // 기존 span을 input으로 변경
            li.replaceChild(input, span);
            li.replaceChild(submitButton, editButton);
        };

        li.appendChild(span);
        if (profile.name === comment.author) {
            li.appendChild(editButton);
            li.appendChild(deleteButton);
        }
        commentList.appendChild(li);
    });
};

// 댓글 수정 함수 (가상의 API 요청)
const updateComment = (id, newContent) => {
    fetch(`/api/comment/edit/${id}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ content: newContent })
    })
        .then(response => response.json())
        .then(data => console.log("댓글 업데이트 성공:", data))
        .catch(error => console.error("댓글 업데이트 실패:", error));
};

// const paintComments = (profile, comments) => {
//     const commentList = document.getElementById("comments");
//     commentList.innerHTML = ""; // 기존 리스트 초기화
//     comments.forEach(comment => {
//         const li = document.createElement("li");
//         li.innerHTML = `
//             <span>${comment.author} : ${comment.content}</span>
//         `;
//         if (profile.name == comment.author) {
//             const button = document.createElement("button");
//             button.textContent = "삭제";
//             button.onclick = () => deleteComment(profile, comment.id); // 삭제 함수 호출
//             li.appendChild(button); // 버튼을 li에 추가
//         }
//         commentList.appendChild(li);
//     });
// };

const paintAddComment = (profile) => {
    document.getElementById("commentForm").addEventListener("submit",
        async (event) => {
        event.preventDefault(); // 기본 폼 제출 막기

        const content = document.getElementById("content").value.trim();
        if (!content) {
            alert("댓글을 입력하세요!");
            return;
        }

        try {
            const response = await fetch(`/api/comment/new/${postId}`, {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: new URLSearchParams({ content })
            });

            if (!response.ok) throw new Error("서버 오류 발생!");

            // ✅ 응답을 받은 후 실행할 함수 호출
            onCommentAdded(profile);
        } catch (error) {
            console.error("댓글 추가 실패:", error);
        }
    });
};

function onCommentAdded(profile) {
    document.getElementById("content").value = ""; // 입력 필드 초기화

    fetch(`/api/comments/${postId}`)
        .then(response => response.json())
        .then(comments => {
            paintComments(profile, comments);
        });
}


const deleteComment = async (profile, id) => {
    await fetch(`/api/comment/delete/${id}`, {method : "POST"})
        .catch(error => console.error("Error deleting comment:", error));
    fetch(`/api/comments/${postId}`)
        .then(response => response.json())
        .then(comments => {
            paintComments(profile, comments);
    });
};