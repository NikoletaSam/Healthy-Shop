let commentContainer = document.getElementById('commentCtnr')
let loadCommentsBtn = document.getElementById('loadComments')
let hideCommentsBtn = document.getElementById('hideComments')

loadCommentsBtn.addEventListener('click', onLoadComments);
hideCommentsBtn.addEventListener('click', onHideComments);

function onLoadComments(event) {
    commentContainer.innerHTML = ''

    var requestOptions = {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    };

    fetch('http://localhost:8080/comments', requestOptions)
        .then(res => res.json())
        .then(res => res.forEach(comment => {
            commentContainer.innerHTML += commentToHtml(comment)
        }))
}

function commentToHtml(comment) {
    let commentHtml = '<div>\n'
    commentHtml += `<h4>${comment.authorFullName}</h4>\n`
    commentHtml += `<p>${comment.created.replace('T', ' ')}<br>`
    commentHtml += `${comment.text}</p>\n`
    commentHtml += '</div>\n'

    return commentHtml
}

function onHideComments(event) {
    commentContainer.innerHTML = ''
}