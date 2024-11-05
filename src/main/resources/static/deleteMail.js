function deleteMail(id) {
  if (confirm("이 기념일을 삭제하시겠습니까?")) {
    fetch(`/mail/delete/${id}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'``
      }
    })
    .then(response => {
      if (response.ok) {
        alert("메일이 성공적으로 삭제되었습니다.");
        document.getElementById(`mail-${id}`).remove();
      } else {
        alert("삭제에 실패했습니다. 다시 시도해 주세요.");
      }
    })
    .catch(error => console.error('Error:', error));
  }
}