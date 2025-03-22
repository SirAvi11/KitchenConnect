function removeMenuItem(menuItemId){
    const foodItemsContainerId = `food-item-template-${menuItemId}`;
    const foodItemsContainer = document.getElementById(foodItemsContainerId);

     // Show the confirmation modal
     const confirmationModal = new bootstrap.Modal(document.getElementById('confirmationModal'));
     confirmationModal.show();
 
     // Handle the "Yes" button click
     document.getElementById('confirmDeleteButton').onclick = async function () {
        // Perform the delete operation
        const isDeleted = await deleteFoodItem(menuItemId);

         if(true){
                const container = foodItemsContainer.closest('.card-body').querySelector('.food-items-container');
                const accordionHeader = foodItemsContainer.closest('.card').querySelector('.card-header');
                foodItemsContainer.remove();
                confirmationModal.hide();
                const itemCount = container.querySelectorAll('.food-item-template').length;
                updateAccordionCount(accordionHeader, itemCount);  
         }
     };
 
     // Handle the "Cancel" button click
     document.getElementById('cancelDeleteButton').onclick = function () {
         confirmationModal.hide(); // Simply close the modal
     };
}

async function deleteFoodItem(menuId){
    try {
        const response = await fetch(`/menu-items/${menuId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Failed to delete menu item.');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to delete menu item. Please try again.');
    }
}

// Function to save the food item to the backend
async function saveFoodItem(payload, imageFile) {
    try {

        // Create a FormData object to send the payload and image file
        const formData = new FormData();
        formData.append('dishName', payload.dishName);
        formData.append('dishDescription', payload.dishDescription);
        formData.append('price', payload.price);
        formData.append('foodType', payload.foodType);
        formData.append('categoryId', payload.categoryId);

        // Append the image file if it exists
        if (imageFile) {
            formData.append('image', imageFile);
        }

        //Append menu id if not null
        if(payload.menuId != null){
            formData.append('menuId', payload.menuId);
        }

        // Send the data to the backend
        const response = await fetch('/menu-items/save-item', {
            method: 'POST',
            body: formData, // Use FormData to send multipart data
        });

        if (!response.ok) {
            throw new Error('Failed to save food item.');
        }

        const data = await response.json();
        return data; // Return the response from the backend
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}

// Function to create a new category
async function createNewCategory(categoryName) {
    const payload = {
        name: categoryName,
    };

    try {
        const response = await fetch('/category', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error('Failed to create category.');
        }

        const data = await response.json();
        return data; // Return the created category
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to create category. Please try again.');
        return null;
    }
}

// Function to update a category
async function updateCategory(categoryId, updatedCategory) {
    const payload = {
        id : categoryId,
        name: updatedCategory
    };

    try {
        const response = await fetch(`/category/${categoryId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error('Failed to update category.');
        }

        const data = await response.json();
        return data; // Return the updated category
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to update category. Please try again.');
        return null;
    }
}

// Function to delete a category
async function deleteCategory(categoryId) {
    try {
        const response = await fetch(`/category/${categoryId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Failed to delete category.');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to delete category. Please try again.');
    }
}

//function to delete accordion category
function deleteAccordion(button,categoryId) {
    // Get the accordion item to be deleted
    const accordionItem = button.closest('.card-header').parentElement;

    // Show the confirmation modal
    const confirmationModal = new bootstrap.Modal(document.getElementById('confirmationModal'));
    confirmationModal.show();

    // Handle the "Yes" button click
    document.getElementById('confirmDeleteButton').onclick = async function () {
        // Perform the delete operation
        const isDeleted = await deleteCategory(categoryId);

        if(isDeleted){
            accordionItem.remove();
            confirmationModal.hide();
        }else{
            alert("Delete Operation Failed!")
        }
    };

    // Handle the "Cancel" button click
    document.getElementById('cancelDeleteButton').onclick = function () {
        confirmationModal.hide(); // Simply close the modal
    };
}

// Function to toggle between edit and save modes
async function toggleHeaderName(button, categoryId) {
    const cardHeader = button.closest('.card-header');
    const accordionButton = cardHeader.querySelector('.accordion-button');
    const categoryNameSpan = accordionButton.querySelector('.flex-grow-1');
    const editButton = cardHeader.querySelector('#edit-info-button');
    const saveButton = cardHeader.querySelector('#save-info-button');

    if (editButton.classList.contains('d-none')) {
        // Currently in Save mode, switch back to Edit mode
        const inputField = accordionButton.querySelector('input');
        const newCategoryName = inputField.value.trim();

        if (newCategoryName) {
            const result = await updateCategory(categoryId, newCategoryName);
            if(result){
                // Update the UI with the new category name
                categoryNameSpan.textContent = `${newCategoryName} (${categoryNameSpan.textContent.split('(')[1]}`;
            } else{
                alert('Failed to update category name. Please try again.');
            }

        }

        // Revert to Edit mode
        editButton.classList.remove('d-none');
        saveButton.classList.add('d-none');
        categoryNameSpan.style.display = 'inline'; // Show the category name
        inputField.remove(); // Remove the input field
    } else {
        // Currently in Edit mode, switch to Save mode
        const currentCategoryName = categoryNameSpan.textContent.split('(')[0].trim();

        // Create an input field for editing
        const inputField = document.createElement('input');
        inputField.type = 'text';
        inputField.placeholder = 'Enter new category name';
        inputField.value = currentCategoryName;
        inputField.classList.add('form-control', 'w-50');

        // Hide the category name and show the input field
        categoryNameSpan.style.display = 'none';
        accordionButton.appendChild(inputField);

        // Toggle buttons
        editButton.classList.add('d-none');
        saveButton.classList.remove('d-none');
    }
}

function previewImage(event, imgElement,foodImageWrapper) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            // Display the uploaded image
            imgElement.src = e.target.result;
            imgElement.classList.remove('d-none');
            foodImageWrapper.classList.add('has-image'); // Add a class to indicate an image is uploaded
        };
        reader.readAsDataURL(file);
    }
}

// Function to toggle between edit and save modes for a food item
function toggleEditFoodItemInfo(button) {
    const foodItemTemplate = button.closest('.food-item-template');
    const editButton = foodItemTemplate.querySelector('#edit-info-button');
    const saveButton = foodItemTemplate.querySelector('#save-info-button');
    const infoFields = foodItemTemplate.querySelectorAll('.info-field');
    const infoInputs = foodItemTemplate.querySelectorAll('.info-input');
    const foodImageUpload = foodItemTemplate.querySelector('[id^="foodImageUpload-"]');
    const foodImageWrapper = foodItemTemplate.querySelector('.food-image-wrapper');
    const vegNonVegInfoField = foodItemTemplate.querySelector('.veg-nonveg-section .info-field');
    const vegNonVegInputs = foodItemTemplate.querySelector('.veg-nonveg-section .info-input');

    if (editButton.classList.contains('d-none')) {
        // Currently in Save mode, switch back to Edit mode

        const foodItemsContainer = button.closest('.card-body').querySelector('.food-items-container');
        const categoryId = foodItemsContainer.getAttribute('data-category-id'); // Retrieve the categoryId

        const dishName = foodItemTemplate.querySelector('.food-name').value;
        const dishDescription = foodItemTemplate.querySelector('.food-description').value;
        const price = foodItemTemplate.querySelector('#price').value;
        const foodType = foodItemTemplate.querySelector('input[name="foodType"]:checked').value;
        const imageFile = foodImageUpload.files[0];
        const menuId = foodItemTemplate.getAttribute("data-menu-id");

        // Prepare the payload
        const payload = {
            dishName,
            dishDescription,
            price: parseFloat(price),
            foodType,
            categoryId,
            menuId
        };

        // Send the data to the backend
        saveFoodItem(payload, imageFile)
            .then((response) => {
                if (response.status === "success") {
                    const savedMenuItem = response.menuId;

                    //update id fields
                    if(!foodItemTemplate.hasAttribute("data-menu-id")){
                        foodItemTemplate.setAttribute("data-menu-id",response.menuId);
                    }

                    if(!foodImageUpload.hasAttribute("data-menu-item-id")){
                        foodImageUpload.setAttribute("data-menu-item-id", response.menuId);
                    }

                    console.log("savedMenuItem", savedMenuItem);
                    // Update the displayed text with the input values
                    infoFields[0].textContent = dishName; // Dish Name
                    infoFields[1].textContent = dishDescription; // Description
                    infoFields[2].textContent = price; // Price
                    vegNonVegInfoField.textContent = foodType === 'veg' ? 'Veg' : 'Non-Veg'; // Veg/Non-Veg

                    // Revert to Presentation mode
                    infoFields.forEach(field => field.style.display = 'inline');
                    infoInputs.forEach(input => input.style.display = 'none');
                    vegNonVegInfoField.style.display = 'inline';
                    vegNonVegInputs.style.display = 'none';
                    editButton.classList.remove('d-none');
                    saveButton.classList.add('d-none');
                    foodImageUpload.disabled = true;
                } else {
                    alert('Failed to save food item. Please try again.');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
            
    } else {
        // Currently in Edit mode, switch to Save mode
        infoFields.forEach((field, index) => {
            infoInputs[index].value = field.textContent; // Set the input value to the current text
            field.style.display = 'none'; // Hide the info field
        });

        // Set the selected radio button based on the info field value
        const selectedFoodType = vegNonVegInfoField.textContent.trim().toLowerCase();
        if (selectedFoodType === 'veg') {
            foodItemTemplate.querySelector('#veg').checked = true;
        } else if (selectedFoodType === 'non-veg') {
            foodItemTemplate.querySelector('#nonVeg').checked = true;
        }

        // Show the radio buttons
        vegNonVegInfoField.style.display = 'none';
        vegNonVegInputs.style.display = 'block';

        // Show the input fields
        infoInputs.forEach(input => input.style.display = 'block');

        // Toggle buttons
        editButton.classList.add('d-none');
        saveButton.classList.remove('d-none');

        //Enable food upload
        foodImageUpload.disabled = false;

    }
}

function getFoodItemTemplate(menuItem) {
    return `
        <div id="food-item-template-${menuItem}" class="food-item-template d-flex align-items-center p-3 border rounded shadow-sm">
            <div class="action-buttons">
                <button id="edit-info-button" class="btn btn-warning text-white cus-btn d-none" onclick="toggleEditFoodItemInfo(this)"><i class="fas fa-edit"></i></button>
                <button id="save-info-button" class="btn btn-warning text-white cus-btn" onclick="toggleEditFoodItemInfo(this)"><i class="fas fa-save"></i></button>
                <button class="btn btn-danger ms-3 remove-food-item cus-btn">-</button>
            </div>
            <div class="inner-menu-content d-flex align-items-center">
                <div class="food-image-wrapper flex-shrink-0" onclick="document.getElementById('foodImageUpload-${menuItem}').click()">
                    <i class="fas fa-plus mb-2 upload-icon"></i>
                    <p class="upload-text">Upload Image</p>
                    <img src="" alt="Food Image" class="food-image-preview d-none" id="food-image-preview-${menuItem}">
                    <input type="file" class="form-control-file food-image-upload d-none" id="foodImageUpload-${menuItem}" data-menu-item-id="${menuItem}">
                </div>
                <div class="food-details flex-grow-1 d-flex flex-column justify-content-between ms-3">
                    <div class="food-info d-flex">
                        <div class="dish-name d-flex flex-column">
                            <label class="text-secondary">Dish Name</label>
                            <p class="info-field bg-light p-2 rounded"></p>
                            <input type="text" class="info-input form-control border rounded mt-1 food-name active" placeholder="Enter Dish Name">
                        </div>
                        <div class="dish-description d-flex flex-column">
                            <label class="text-secondary">Description</label>
                            <p class="info-field bg-light p-2 rounded"></p>
                            <textarea class="info-input form-control border rounded mt-1 food-description active" placeholder="Enter Dish Description"></textarea>
                        </div>
                    </div>
                    <div class="food-options d-flex justify-content-between mt-2">
                        <div class="price">
                            <label class="text-secondary">Price</label>
                            <p class="info-field bg-light p-2 rounded"></p>
                            <input id="price" type="number" class="info-input form-control border rounded active mt-1 w-50" placeholder="Price">
                        </div>
                        <div class="veg-nonveg-section d-flex flex-column">
                            <p class="info-field bg-light p-2 rounded"></p>
                            <div class="info-input active">
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="foodType" id="veg" value="veg">
                                    <label class="form-check-label text-success">Veg</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="foodType" id="nonVeg" value="non-veg">
                                    <label class="form-check-label text-danger">Non-Veg</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>`;
}

// Function to update the count in the accordion header
function updateAccordionCount(accordionHeader, count) {
    const titleText = accordionHeader.querySelector('.flex-grow-1');
    const currentText = titleText.textContent.split('(')[0].trim();
    titleText.textContent = `${currentText} (${count})`;
}

document.addEventListener('DOMContentLoaded', function () {

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
            newFoodItem.innerHTML = getFoodItemTemplate("new");

            // Append the new food item to the container
            foodItemsContainer.prepend(newFoodItem);

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
            const foodImageUpload = foodItemsContainer.querySelector('[id^="foodImageUpload-"]');
            const foodImageWrapper = foodItemsContainer.querySelector('.food-image-wrapper');
            const foodImagePreview = foodImageWrapper.querySelector('[id^="food-image-preview-"]');
            const uploadIcon = foodImageWrapper.querySelector('.upload-icon');
            const uploadText = foodImageWrapper.querySelector('.upload-text');

            foodImageUpload.addEventListener('change', function (event) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        // Display the uploaded image
                        foodImagePreview.src = e.target.result;
                        foodImagePreview.classList.remove('d-none');
                        foodImageWrapper.classList.add('has-image'); // Add a class to indicate an image is uploaded
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

    addCategoryBtn.addEventListener('click', async function () {
        const categoryName = categoryInput.value.trim();

        if (categoryName) {
            // Check if the category name already exists
            if (isCategoryNameExists(categoryName)) {
                alert('A category with this name already exists. Please use a different name.');
                return;
            }

            // Save the category to the database
            const result = await createNewCategory(categoryName);
            if(result.status == "success"){
                // Create a new accordion item
                const newAccordionItem = document.createElement('div');
                newAccordionItem.classList.add('card');

                // Generate a unique ID for the new accordion item
                const uniqueId = `category-${result.id}`;
                const collapseId = `collapse-${result.id}`;

                // Populate the new accordion item
                newAccordionItem.innerHTML = `
                    <div class="card-header" id="${uniqueId}">
                        <h2 class="mb-0 d-flex outer-header">
                            <button class="btn btn-link text-warning font-weight-bold accordion-button d-flex justify-content-start w-100 align-items-center" 
                                type="button" 
                                data-toggle="collapse" 
                                data-target="#${collapseId}" 
                                aria-expanded="true" 
                                aria-controls="${collapseId}">
                            
                                <span class="d-flex align-items-center justify-content-between header-icon-name">
                                    <i class="fas fa-arrow-down me-2"></i>
                                    <span class="flex-grow-1 text-center">${categoryName} (0)</span>
                                </span>
                            </button>
                            <div class="header-buttons">
                                <button id="edit-info-button" class="btn btn-warning text-white cus-btn" onclick="toggleHeaderName(this, ${result.id})">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button id="save-info-button" class="btn btn-warning text-white cus-btn d-none" onclick="toggleHeaderName(this, ${result.id})">
                                    <i class="fas fa-save"></i>
                                </button>
                                <button class="btn btn-danger btn-sm delete-accordion cus-btn" onclick="deleteAccordion(this, ${result.id})">
                                    <i class="fas fa-minus"></i>
                                </button>
                            </div>
                        </h2>
                    </div>
                    <div id="${collapseId}" class="collapse" aria-labelledby="${uniqueId}" data-parent="#categoriesContainer">
                        <div class="card-body">
                            <button class="btn btn-success add-food-item mb-2">Add Item +</button>
                            <div class="food-items-container" data-category-id="${result.id}"></div>
                        </div>            
                    </div>
                `;

                // Insert the new accordion item at the top of the container
                categoriesContainer.prepend(newAccordionItem);

                // Attach event listener to the new "Add Item +" button
                const addFoodItemButton = newAccordionItem.querySelector('.add-food-item');
                setupAddFoodItemButton(addFoodItemButton);

                // Clear the input field
                categoryInput.value = '';
            }

        } else {
            alert('Please enter a category name.');
        }
    });

    // Function to toggle arrow icon in accordion header
    document.querySelectorAll(".accordion-button").forEach(button => {
        button.addEventListener("click", function () {
            const icon = this.querySelector("i.fa-arrow-down, i.fa-arrow-up");
            if(icon.classList.contains("fa-arrow-down")){
                icon.classList.remove("fa-arrow-down");
                icon.classList.add("fa-arrow-up");
            }else{
                icon.classList.remove("fa-arrow-up");
                icon.classList.add("fa-arrow-down");
            }
        });
    });
    
    //Event listnener for existing 'Add Item +' buttons
    document.querySelectorAll(".add-food-item").forEach(button =>{
        setupAddFoodItemButton(button);
    })

    //Event listner for image upload
    document.querySelectorAll('.food-image-upload').forEach(fileInput => {
        fileInput.addEventListener('change', function (event) {
            const menuItemId = this.getAttribute('data-menu-item-id');
            const foodImageWrapper = this.closest('.food-image-wrapper');
            const imgElement = document.getElementById(`food-image-preview-${menuItemId}`);
            previewImage(event, imgElement,foodImageWrapper);
        });
    });
    
});


