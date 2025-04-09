document.addEventListener('DOMContentLoaded', function () {
    // Get all "View" buttons
    const viewButtons = document.querySelectorAll('.view-user-btn');

    // Add click event listener to each button
    viewButtons.forEach(button => {
        button.addEventListener('click', function () {
            const userId = button.getAttribute('data-user-id');
            const role = button.getAttribute('data-user-role');
            const username = button.getAttribute('data-username');
            const firstname = button.getAttribute('data-firstname');
            const lastname = button.getAttribute('data-lastname');
            const phonenumber = button.getAttribute('data-phonenumber');
            const address = button.getAttribute('data-address');
            const email = button.getAttribute('data-email');

            // Hide chef section by default
            document.getElementById('chefDetailsSection').classList.add('d-none');

           
            document.getElementById('modal-userId').innerText = userId;
            document.getElementById('modal-username').innerText = username;
            document.getElementById('modal-firstname').innerText = firstname;
            document.getElementById('modal-lastname').innerText = lastname;
            document.getElementById('modal-phonenumber').innerText = phonenumber;
            document.getElementById('modal-address').innerText = address;
            document.getElementById('modal-email').innerText = email;
            document.getElementById('modal-role').innerText = role;

            if (role === 'CHEF') {
                fetch(`/chef/get/${userId}`)
                    .then(response => {
                        if (!response.ok) throw new Error('Chef not found');
                        return response.json();
                    })
                    .then(chefData => {
                        // Update Chef Details
                        const imageUrl = chefData.chefProfilePicture || ''; // assume it's a full image URL or base64
                        const imgHTML = imageUrl ? `<img src="${imageUrl}" alt="Chef Image" class="img-fluid rounded" style="max-height: 150px;">` : 'No Image';
                        document.getElementById('profile_image').innerHTML = imgHTML;

                        document.getElementById('biography').textContent = chefData.biography || 'N/A';
                        document.getElementById('favourites').textContent = chefData.favouriteDishes || 'N/A';

                        // Show chef section
                        document.getElementById('chefDetailsSection').classList.remove('d-none');

                        const modal = new bootstrap.Modal(document.getElementById('userDetailsModal'));
                        modal.show();
                    })
                    .catch(error => {
                        console.error(error);
                        alert('Error fetching chef details.');
                        const modal = new bootstrap.Modal(document.getElementById('userDetailsModal'));
                        modal.show();
                    });
            }
            const modal = new bootstrap.Modal(document.getElementById('userDetailsModal'));
            modal.show();
        });
    });

    // Get filter elements
        const userId = document.getElementById('searchUserId');
        const searchButton = document.getElementById('searchButton');
        const filterStatus = document.getElementById('filterStatus');
        const startDate = document.getElementById('startDate');
        const endDate = document.getElementById('endDate');
        const refreshButton = document.getElementById('refreshButton');
        const tableRows = document.querySelectorAll('tbody tr');
    
        // Function to filter table rows
        function filterTable() {
            const searchText = userId.value.trim().toLowerCase();
            const selectedStatus = filterStatus.value;
            const startDateValue = startDate.value;
            const endDateValue = endDate.value;
    
            tableRows.forEach(row => {
                const orderNumber = row.querySelector('td:nth-child(2)').textContent.trim().toLowerCase();
                const orderDate = row.querySelector('td:nth-child(4)').textContent.trim();
    
                // Convert order date to a comparable format (dd-MM-yyyy to yyyy-MM-dd)
                const orderDateFormatted = orderDate.split('-').reverse().join('-');
    
                // Get the status from the dropdown or span
                const statusCell = row.querySelector('td:nth-child(6)');
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

        const userModal = document.getElementById('userDetailsModal');
        userModal.addEventListener('hidden.bs.modal', () => {
            // Absolute fallback
            document.body.classList.remove('modal-open');
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
        });

});