function showImg(img_id) {
    const imgElement = document.getElementById(img_id);

    // 确保 imgElement 存在
    if (!imgElement) {
        console.error(`元素 ID ${img_id} 未找到`);
        return;
    }

    imgElement.style.display = 'inline';
}


function hideImg(img_id) {
    const imgElement = document.getElementById(img_id);

    // 确保 imgElement 存在
    if (!imgElement) {
        console.error(`元素 ID ${img_id} 未找到`);
        return;
    }

    // 隐藏图片
    imgElement.style.display = 'none';
}

function delReply(replyId, topicId) {
    if(window.confirm("是否确认删除?")) {
        window.location.href='reply.do?operate=delReply&replyId='+replyId+'&topicId='+topicId;
    }
}