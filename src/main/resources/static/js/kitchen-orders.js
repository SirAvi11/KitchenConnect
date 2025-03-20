document.addEventListener('DOMContentLoaded', function () {
    // Get all "View" buttons
    const viewButtons = document.querySelectorAll('.view-order-btn');

    // Add click event listener to each button
    viewButtons.forEach(button => {
        button.addEventListener('click', function () {
            // Extract data from the button's data attributes
            const orderId = button.getAttribute('data-order-id');
            const customerName = button.getAttribute('data-customer-name');
            const customerPhone = button.getAttribute('data-customer-phone');
            const customerAddress = button.getAttribute('data-customer-address');
            const orderDate = button.getAttribute('data-order-date');
            const orderTotal = parseFloat(button.getAttribute('data-order-total'));
            const orderStatus = button.getAttribute('data-order-status');
            const paymentStatus = button.getAttribute('data-payment-status');
            const kitchenDeliveryFees = parseFloat(button.getAttribute('data-kitchen-delivery'));

            // Populate the modal with the extracted data
            document.getElementById('customerName').textContent = customerName;
            document.getElementById('customerPhone').textContent = customerPhone;
            document.getElementById('customerAddress').textContent = customerAddress;
            document.getElementById('orderId').textContent = orderId;
            document.getElementById('orderDate').textContent = orderDate;
            document.getElementById('orderTotal').textContent = 'Rs ' + orderTotal;
            document.getElementById('orderStatus').textContent = orderStatus;
            document.getElementById('paymentStatus').textContent = paymentStatus;

            // Clear the order items table
            const orderItemsTableBody = document.getElementById('orderItemsTableBody');
            orderItemsTableBody.innerHTML = '';

            // Fetch order items (assuming you have an endpoint to get order items)
            fetch(`/orders/${orderId}/items`)
                .then(response => response.json())
                .then(orderItems => {
                    let subtotal = 0; // Initialize subtotal

                    // Populate the order items table
                    orderItems.forEach(item => {
                        const row = document.createElement('tr');
                        const itemTotal = item.price * item.quantity; // Calculate total for each item
                        subtotal += itemTotal; // Add to subtotal
                        row.innerHTML = `
                            <td>${item.menuItem.name}</td>
                            <td>${item.quantity}</td>
                            <td>Rs ${item.price}</td>
                            <td>Rs ${itemTotal}</td>
                        `;
                        orderItemsTableBody.appendChild(row);
                    });
                    // Add rows for tax, delivery fee, and platform fee
                    const taxRow = document.createElement('tr');
                    taxRow.innerHTML = `
                        <td colspan="3" style="text-align: right;"><strong>Tax (5%):</strong></td>
                        <td><strong>Rs ${(subtotal * 0.05).toFixed(2)}</strong></td>
                    `;
                    orderItemsTableBody.appendChild(taxRow);

                    const deliveryFeeRow = document.createElement('tr');
                    deliveryFeeRow.innerHTML = `
                        <td colspan="3" style="text-align: right;"><strong>Delivery Fee:</strong></td>
                        <td><strong>Rs ${kitchenDeliveryFees.toFixed(2)}</strong></td>
                    `;
                    orderItemsTableBody.appendChild(deliveryFeeRow);

                    const platformFeeRow = document.createElement('tr');
                    platformFeeRow.innerHTML = `
                        <td colspan="3" style="text-align: right;"><strong>Platform Fee:</strong></td>
                        <td><strong>Rs 5.00</strong></td>
                    `;
                    orderItemsTableBody.appendChild(platformFeeRow);

                    // Add a row for the subtotal
                    const subtotalRow = document.createElement('tr');
                    subtotalRow.innerHTML = `
                        <td colspan="3" style="text-align: right;"><strong>Subtotal:</strong></td>
                        <td><strong>Rs ${orderTotal.toFixed(2)}</strong></td>
                    `;
                    orderItemsTableBody.appendChild(subtotalRow);

                })
                .catch(error => {
                    console.error('Error fetching order items:', error);
                });

            // Show the modal
            const modal = new bootstrap.Modal(document.getElementById('orderDetailsModal'));
            modal.show();
        });
    });

     // Get all status dropdowns
    const statusDropdowns = document.querySelectorAll('.order-status-dropdown');

    // Add change event listener to each dropdown
    statusDropdowns.forEach(dropdown => {
        dropdown.addEventListener('change', function () {
            const orderId = dropdown.getAttribute('data-order-id');
            const newStatus = dropdown.value;

            // Send an AJAX request to update the order status
            fetch('/orders/update-order-status', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    orderId: orderId,
                    newStatus: newStatus
                })
            })
            .then(response => {
                if (response.ok) {
                    // Optionally, refresh the page or update the UI dynamically
                    // If the new status is COMPLETED, CANCELLED, or DELIVERED, replace the dropdown with a span
                window.location.href = window.location.pathname + '?tab=kitchen-orders';
                if (newStatus === 'READY' || newStatus === 'CANCELLED' || newStatus === 'DELIVERED') {
                    const statusCell = dropdown.closest('td');
                    let badgeClass = '';
                    let statusText = '';

                    switch (newStatus) {
                        case 'READY':
                            badgeClass = 'bg-green';
                            statusText = 'READY';
                            break;
                        case 'CANCELLED':
                            badgeClass = 'bg-danger';
                            statusText = 'CANCELLED';
                            break;
                        case 'DELIVERED':
                            badgeClass = 'bg-primary';
                            statusText = 'DELIVERED';
                            break;
                    }

                    // Replace the dropdown with a span
                    statusCell.innerHTML = `<span class="badge ${badgeClass}">${statusText}</span>`;
                } else {
                    const selectElement = document.getElementById('filterStatus');
                    // Replace the dropdown with a span
                    selectElement.innerHTML = `<span class="badge">${newStatus}</span>`;
                    // Show success toast
                    successToast.show();

                }
                } else {
                    // Show error toast
                    errorToast.show();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                errorToast.show();
            });
        });
    });
    // Get filter elements
    const searchOrderNumber = document.getElementById('searchOrderNumber');
        const searchButton = document.getElementById('searchButton');
        const filterStatus = document.getElementById('filterStatus');
        const startDate = document.getElementById('startDate');
        const endDate = document.getElementById('endDate');
        const refreshButton = document.getElementById('refreshButton');
        const tableRows = document.querySelectorAll('tbody tr');
    
        // Function to filter table rows
        function filterTable() {
            const searchText = searchOrderNumber.value.trim().toLowerCase();
            const selectedStatus = filterStatus.value;
            const startDateValue = startDate.value;
            const endDateValue = endDate.value;
    
            tableRows.forEach(row => {
                const orderNumber = row.querySelector('td:nth-child(2)').textContent.trim().toLowerCase();
                const orderDate = row.querySelector('td:nth-child(4)').textContent.trim();
    
                // Convert order date to a comparable format (dd-MM-yyyy to yyyy-MM-dd)
                const orderDateFormatted = orderDate.split('-').reverse().join('-');
    
                // Get the status from the dropdown or span
                const statusCell = row.querySelector('td:nth-child(8)');
                let status = '';
    
                if (statusCell.querySelector('select')) {
                    // If the status is in a dropdown, get the selected value
                    status = statusCell.querySelector('select').value;
                } else if (statusCell.querySelector('span')) {
                    // If the status is in a span, get the text content
                    status = statusCell.querySelector('span').textContent.trim();
                }
    
                // Check if the row matches the filters
                const matchesSearch = orderNumber.includes(searchText);
                const matchesStatus = selectedStatus === 'ALL' || status === selectedStatus;
                const matchesDateRange = (!startDateValue || orderDateFormatted >= startDateValue) &&
                                        (!endDateValue || orderDateFormatted <= endDateValue);
    
                // Show or hide the row based on the filters
                if (matchesSearch && matchesStatus && matchesDateRange) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
    
        // Add event listeners
        searchButton.addEventListener('click', filterTable);
        filterStatus.addEventListener('change', filterTable);
        startDate.addEventListener('change', filterTable);
        endDate.addEventListener('change', filterTable);
    
        // Refresh button to reset filters
        refreshButton.addEventListener('click', function () {
            searchOrderNumber.value = '';
            filterStatus.value = 'ALL';
            startDate.value = '';
            endDate.value = '';
            filterTable(); // Reapply filters (which will show all rows)
        });
    
        // Initial filter application (optional)
        filterTable();
});