document.addEventListener("DOMContentLoaded", function () {
    fetch("/api/post/all")
        .then(response => response.json())
        .then(data => {
            const postList = document.getElementById("posts");
            postList.innerHTML = ""; // 기존 리스트 초기화

            data.forEach(post => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <span>제목 : ${post.title}</span>
                    <small>작성자 : ${post.author}</small>
                `;
                li.addEventListener("click", () => {
                    window.location.href = `post/${post.id}`;
                });
                postList.appendChild(li);
            });
        })
        .catch(error => console.error("Error fetching posts:", error));
});

async function searchPosts() {
    const searchQuery = document.getElementById("searchInput").value;

    try {
        if (searchQuery == "") {
            fetch("/api/post/all")
                .then(response => response.json())
                .then(data => {
                    const postList = document.getElementById("posts");
                    postList.innerHTML = ""; // 기존 리스트 초기화

                    data.forEach(post => {
                        const li = document.createElement("li");
                        li.innerHTML = `
                    <span>제목 : ${post.title}</span>
                    <small>작성자 : ${post.author}</small>
                `;
                        li.addEventListener("click", () => {
                            window.location.href = `post/${post.id}`;
                        });
                        postList.appendChild(li);
                    });
                })
                .catch(error => console.error("Error fetching posts:", error));
        } else {
            fetch("/api/post/query" + "?condition=" + searchQuery)
                .then(response => response.json())
                .then(data => {
                    const postList = document.getElementById("posts");
                    postList.innerHTML = ""; // 기존 리스트 초기화

                    data.forEach(post => {
                        const li = document.createElement("li");
                        li.innerHTML = `
                    <span>제목 : ${post.title}</span>
                    <small>작성자 : ${post.author}</small>
                    `;
                        li.addEventListener("click", () => {
                            window.location.href = `post/${post.id}`;
                        });
                        postList.appendChild(li);
                    });
                })
                .catch(error => console.error("Error fetching posts:", error));
        }
    } catch (error) {
        console.error("게시물 검색 중 오류 발생:", error);
    }
}
