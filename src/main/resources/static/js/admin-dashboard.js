function updateKitchenStatus(kitchenId,isApproved) {
    fetch('/kitchens/statusUpdate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ kitchenId: kitchenId, isApproved: Boolean(isApproved)})
    })
    .then(response => response.json())
    .then(data => console.log("Session Kitchen Updated:", data.status))
    .catch(error => console.error("Error updating cart:", error));
  }