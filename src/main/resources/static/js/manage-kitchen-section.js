document.getElementById('addCategoryBtn').addEventListener('click', function() {
    const categoryName = document.getElementById('categoryInput').value;
    if (categoryName) {
        const categoryCount = 0; // Initialize count
        const categoryCard = document.createElement('div');
        categoryCard.className = 'card mt-3';
        categoryCard.innerHTML = `
            <div class="card-header">
                ${categoryName} (${categoryCount})
                <button class="btn btn-danger btn-sm float-right remove-category">-</button>
            </div>
            <div class="card-body">
                <button class="btn btn-success add-food-item">+</button>
                <div class="food-item-template d-flex align-items-center p-3 border rounded shadow-sm">
                    <!-- Image Upload (Left Side) -->
                    <div class="food-image-wrapper flex-shrink-0">
                        <input type="file" class="form-control-file d-none" id="foodImageUpload">
                        <label for="foodImageUpload" class="food-image-placeholder d-flex align-items-center justify-content-center">
                            Upload Image
                        </label>
                    </div>

                    <!-- Food Details (Right Side) -->
                    <div class="food-details flex-grow-1 d-flex flex-column justify-content-between ms-3">
                        <!-- Name & Description -->
                        <div class="food-info d-flex">
                            <input type="text" class="form-control food-name me-2" placeholder="Food Name">
                            <textarea class="form-control food-description" placeholder="Food Description"></textarea>
                        </div>

                        <!-- Price & Veg/Non-Veg -->
                        <div class="food-options d-flex align-items-center gap-2 mt-2">
                            <input type="number" class="form-control w-25" placeholder="Price">
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="foodType" id="veg" value="veg">
                                <label class="form-check-label text-success" for="veg">Veg</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="foodType" id="nonVeg" value="non-veg">
                                <label class="form-check-label text-danger" for="nonVeg">Non-Veg</label>
                            </div>
                        </div>
                    </div>

                    <!-- Remove Button (Right Side) -->
                    <button class="btn btn-danger ms-3 remove-food-item">-</button>
                </div>

                <div class="food-items-container"></div>
            </div>
        `;
        document.getElementById('categoriesContainer').appendChild(categoryCard);
        document.getElementById('categoryInput').value = '';

        // Add event listener for adding food items
        categoryCard.querySelector('.add-food-item').addEventListener('click', function() {
            const foodItemTemplate = categoryCard.querySelector('.food-item-template');
            const foodItemClone = foodItemTemplate.cloneNode(true);
            foodItemClone.style.display = 'block';
            categoryCard.querySelector('.food-items-container').appendChild(foodItemClone);
        });

        // Add event listener for removing category
        categoryCard.querySelector('.remove-category').addEventListener('click', function() {
            categoryCard.remove();
        });

        // Add event listener for removing food items
        categoryCard.querySelector('.food-items-container').addEventListener('click', function(e) {
            if (e.target.classList.contains('remove-food-item')) {
                e.target.closest('.food-item-template').remove();
            }
        });
    }
});