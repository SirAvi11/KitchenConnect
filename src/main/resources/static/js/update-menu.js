document.addEventListener('DOMContentLoaded', function () {
    // Define the food item template
    const foodItemTemplate = `
        <div class="food-item-template d-flex align-items-center p-3 border rounded shadow-sm">
            <div class="action-buttons">
                <!-- Edit and save button -->
                <button id="edit-info-button" class="btn btn-warning text-white" onclick="toggleEditInfo()"><i class="fas fa-edit"></i></button>
                <button id="save-info-button" class="btn btn-warning text-white d-none" onclick="toggleEditInfo()"><i class="fas fa-save"></i></button>
            </div>
            <div class="inner-menu-content d-flex align-items-center">
                <!-- Image Upload (Left Side) -->
                <div class="food-image-wrapper flex-shrink-0">
                    <i class="fas fa-plus mb-2"></i>
                    <p>Upload Image</p>
                    <input type="file" class="form-control-file d-none" id="foodImageUpload">
                </div>
                <!-- Food Details (Right Side) -->
                <div class="food-details flex-grow-1 d-flex flex-column justify-content-between ms-3">
                    <!-- Name & Description -->
                    <div class="food-info d-flex">
                        <div class="dish-name">
                            <label class="text-secondary" for="categoryInput">Dish Name</label>
                            <p class="info-field bg-light p-2 rounded active"  th:text="Dish Name">Dish Name</p>
                            <input type="text" class="info-input form-control border rounded mt-1 food-name" placeholder="Enter Dish Name">
                        </div>
                        <div class="dish-description">
                            <label class="text-secondary" for="categoryInput">Description</label>
                            <p class="info-field bg-light p-2 rounded active"  th:text="Dish Name">Description</p>
                            <textarea class="info-input form-control border rounded mt-1  food-description" placeholder="Enter Dish Description"></textarea>
                        </div>
                    </div>
                    <!-- Price & Veg/Non-Veg -->
                    <div class="food-options d-flex justify-content-between mt-2">
                        <div class="price">
                            <label class="text-secondary" for="price">Price</label>
                            <p class="info-field bg-light p-2 rounded active"  th:text="Dish Name">Description</p>
                            <input type="number" id="price" class="info-input form-control border rounded mt-1 w-50" placeholder="Price">
                        </div>
                        <div class="veg-nonveg-section d-flex flex-column">
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
                </div>
                <!-- Remove Button (Right Side) -->
                <button class="btn btn-danger ms-3 remove-food-item">-</button>
            </div>
        </div>
    `;

    // Function to update the count in the accordion header
    function updateAccordionCount(accordionHeader, count) {
        const titleText = accordionHeader.querySelector('.flex-grow-1');
        const currentText = titleText.textContent.split('(')[0].trim();
        titleText.textContent = `${currentText} (${count})`;
    }

    // Function to check if a category name already exists
    function isCategoryNameExists(categoryName) {
        const accordionHeaders = document.querySelectorAll('.card-header .flex-grow-1');
        for (const header of accordionHeaders) {
            const existingName = header.textContent.split('(')[0].trim();
            if (existingName.toLowerCase() === categoryName.toLowerCase()) {
                return true;
            }
        }
        return false;
    }

    // Function to set up event listeners for "Add Item +" button
    function setupAddFoodItemButton(button) {
        button.addEventListener('click', function () {
            const foodItemsContainer = button.closest('.card-body').querySelector('.food-items-container');
            const accordionHeader = button.closest('.card').querySelector('.card-header');

            // Create a new food item from the template
            const newFoodItem = document.createElement('div');
            newFoodItem.innerHTML = foodItemTemplate;

            // Append the new food item to the container
            foodItemsContainer.appendChild(newFoodItem);

            // Update the count in the accordion header
            const itemCount = foodItemsContainer.querySelectorAll('.food-item-template').length;
            updateAccordionCount(accordionHeader, itemCount);

            // Add event listener to the remove button
            const removeButton = newFoodItem.querySelector('.remove-food-item');
            removeButton.addEventListener('click', function () {
                foodItemsContainer.removeChild(newFoodItem);
                // Update the count in the accordion header
                const updatedCount = foodItemsContainer.querySelectorAll('.food-item-template').length;
                updateAccordionCount(accordionHeader, updatedCount);
            });

            // Add event listener to the image upload input
            const foodImageUpload = newFoodItem.querySelector('.food-image-upload');
            const foodImageWrapper = newFoodItem.querySelector('.food-image-wrapper');

            foodImageUpload.addEventListener('change', function (event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        foodImageWrapper.innerHTML = `<img src="${e.target.result}" alt="Food Image" class="img-fluid">`;
                    };
                    reader.readAsDataURL(file);
                }
            });
        });
    }

    // Add Category
    const addCategoryBtn = document.getElementById('addCategoryBtn');
    const categoryInput = document.getElementById('categoryInput');
    const categoriesContainer = document.getElementById('categoriesContainer');

    addCategoryBtn.addEventListener('click', function () {
        const categoryName = categoryInput.value.trim();

        if (categoryName) {
            // Check if the category name already exists
            if (isCategoryNameExists(categoryName)) {
                alert('A category with this name already exists. Please use a different name.');
                return;
            }

            // Create a new accordion item
            const newAccordionItem = document.createElement('div');
            newAccordionItem.classList.add('card');

            // Generate a unique ID for the new accordion item
            const uniqueId = `heading${Date.now()}`;
            const collapseId = `collapse${Date.now()}`;

            // Populate the new accordion item
            newAccordionItem.innerHTML = `
                <div class="card-header" id="${uniqueId}">
                    <h2 class="mb-0 d-flex">
                        <button class="btn btn-link text-warning font-weight-bold accordion-button d-flex justify-content-between w-100 align-items-center" 
                            type="button" 
                            data-toggle="collapse" 
                            data-target="#${collapseId}" 
                            aria-expanded="true" 
                            aria-controls="${collapseId}">
                        
                            <span class="d-flex align-items-center justify-content-between w-100">
                                <i class="fas fa-arrow-down me-2"></i>
                                <span class="flex-grow-1 text-center">${categoryName} (0)</span>
                            </span>
                    
                            <button class="btn btn-danger btn-sm delete-accordion" onclick="deleteAccordion(this)">
                                <i class="fas fa-minus"></i>
                            </button>
                        </button>
                    </h2>
                </div>
                <div id="${collapseId}" class="collapse" aria-labelledby="${uniqueId}" data-parent="#categoriesContainer">
                    <div class="card-body">
                        <button class="btn btn-success add-food-item mb-2">Add Item +</button>
                        <div class="food-items-container"></div>
                    </div>            
                </div>
            `;

            // Insert the new accordion item at the top of the container
            categoriesContainer.prepend(newAccordionItem);

            // Clear the input field
            categoryInput.value = '';

            // Set up event listeners for the new "Add Item +" button
            const newAddFoodItemButton = newAccordionItem.querySelector('.add-food-item');
            setupAddFoodItemButton(newAddFoodItemButton);

            // Set up event listener for the delete accordion button
            const deleteAccordionButton = newAccordionItem.querySelector('.delete-accordion');
            deleteAccordionButton.addEventListener('click', function () {
                categoriesContainer.removeChild(newAccordionItem);
            });
        } else {
            alert('Please enter a category name.');
        }
    });

    // Function to toggle arrow icon in accordion header
    document.querySelectorAll(".accordion-button").forEach(button => {
        button.addEventListener("click", function () {
            const icon = this.querySelector("i.fa-arrow-down, i.fa-arrow-up");
            if (this.getAttribute("aria-expanded") === "true") {
                icon.classList.remove("fa-arrow-down");
                icon.classList.add("fa-arrow-up");
            } else {
                icon.classList.remove("fa-arrow-up");
                icon.classList.add("fa-arrow-down");
            }
        });
    });

    // Function to delete an accordion section
    function deleteAccordion(button) {
        const accordionItem = button.closest('.card');
        accordionItem.remove();
    }
});