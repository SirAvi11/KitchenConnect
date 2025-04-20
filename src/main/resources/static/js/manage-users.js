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
        const searchButton = document.getElementById('userSearchButton');
        const filterUserRole = document.getElementById('filterUserRole');
        const refreshButton = document.getElementById('userRefreshButton');
        const tableRows = document.querySelectorAll('#userTable tbody tr');
    
        // Function to filter table rows
        function filterTable() {
            const searchText = userId.value.trim().toLowerCase();
            const selectedRole = filterUserRole.value;
        
            tableRows.forEach(row => {
                const viewBtn = row.querySelector('.view-user-btn');
                const rowUserId = viewBtn.getAttribute('data-user-id').toLowerCase();
                const rowRole = viewBtn.getAttribute('data-user-role');
        
                const matchesUserId = rowUserId.includes(searchText);
                const matchesRole =
                    selectedRole === 'ALL' ||
                    rowRole === selectedRole ||
                    (selectedRole === 'FOOD_LOVER' && rowRole === 'FOOD_LOVER');
        
                if (matchesUserId && matchesRole) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
        
        
    
        // Add event listeners
        searchButton.addEventListener('click', filterTable);
        filterUserRole.addEventListener('change', filterTable);
    
        // Refresh button to reset filters
        refreshButton.addEventListener('click', function () {
            userId.value = '';
            filterUserRole.value = 'ALL';
            filterTable(); // Show all users again
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