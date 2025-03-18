package com.kitchenconnect.kitchen.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kitchenconnect.kitchen.DTO.KitchenRequest;
import com.kitchenconnect.kitchen.DTO.KitchenStatusUpdateRequest;
import com.kitchenconnect.kitchen.entity.Category;
import com.kitchenconnect.kitchen.entity.Chef;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.MenuItem;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.KitchenStatus;
import com.kitchenconnect.kitchen.service.CategoryService;
import com.kitchenconnect.kitchen.service.ChefService;
import com.kitchenconnect.kitchen.service.KitchenService;
import com.kitchenconnect.kitchen.service.MenuItemService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/kitchens")
public class KitchenController {
    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private ChefService chefService;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/kitchenRequest/";

    @GetMapping
    public String showAllKitchens(Model model) {
        List<Kitchen> allKitchens = kitchenService.getAllKitchens();

        model.addAttribute("kitchens", allKitchens);
        return "kitchens";
    }

    @GetMapping("/kitchen-registration")
    public String showKitchenRegistration(Model model, HttpSession session) {
        // Get the logged-in user from session
        User sessionUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("kitchenRequest", new KitchenRequest());

        if(checkKitchenStatus(sessionUser)){
            model.addAttribute("successMessage", "Your kitchen registration is under process. Please wait for approval.");
        }

        return "kitchenRegistration";
    }

    @GetMapping("/{id}")
    public String showKitchenDetails(@PathVariable Long id, Model model, HttpSession session) {
        Kitchen kitchen = kitchenService.getKitchenById(id);

        if (kitchen != null) {
            // Fetch the chef's name from the associated Chef entity
            String chefName = kitchen.getUser().getFirstname() + " " + kitchen.getUser().getLastname();
            Chef chef = chefService.getChefByKitchenId(kitchen.getKitchenId());

           // Fetch Food Categories and Categories Items (Menu)
            Map<String, List<MenuItem>> menuItems = new HashMap<>();

            List<Category> foodCategories = categoryService.getCategoriesByKitchen(id);

            if (foodCategories != null && !foodCategories.isEmpty()) {
                for (Category category : foodCategories) {
                    menuItems.put(category.getName(), menuItemService.getMenuItemsByCategory(category.getId()));
                }
            }

            // Retrieve cart from session
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }

            model.addAttribute("kitchen", kitchen);
            model.addAttribute("chefName", chefName); // Add chef's name to the model
            model.addAttribute("chefId", chef.getChefId()); // Add chef's Id to the model
            model.addAttribute("menuItems", menuItems); // Add menu items to model
            model.addAttribute("cartItems", cart);


            return "kitchenpage";
        }
        
        return "redirect:/"; // Redirect to homepage if kitchen not found
    }

    @PostMapping("/submitKitchenForm")
    public String saveKitchenRequest(@ModelAttribute KitchenRequest kitchenRequest,
                                    @RequestParam("menuImages") List<MultipartFile> menuImages,
                                    @RequestParam("kitchenImage") MultipartFile kitchenImage,
                                    @RequestParam("fssaiDocument") MultipartFile fssaiDocument,
                                    @RequestParam("panDocument") MultipartFile panDocument,
                                    @RequestParam("selectedCuisines") String selectedCuisines,
                                    @RequestParam("openDays") List<String> openDays,
                                    RedirectAttributes redirectAttributes,
                                    Model model,
                                    HttpSession session) throws IOException {

        try {
            // Get the logged-in user from session
            User sessionUser = (User) session.getAttribute("loggedInUser");

            if (sessionUser == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "No user is logged in.");
                return "redirect:/kitchens/kitchen-registration"; // Redirect to registration
            }

            // Process the cuisines as a comma-separated string
            kitchenRequest.setSelectedCuisines(Arrays.asList(selectedCuisines.split(",")));
            kitchenRequest.setOpenDays(openDays);
            kitchenRequest.setUserId(sessionUser.getId());

            // Save kitchen image and set its path
            if (!kitchenImage.isEmpty()) {
                String kitchenImagePath = saveToDisk(kitchenImage, sessionUser.getId());
                kitchenRequest.setKitchenImagePath(kitchenImagePath);
            }

            // Save menu images and set their paths
            List<String> menuImagePaths = new ArrayList<>();
            for (MultipartFile menuImage : menuImages) {
                if (!menuImage.isEmpty()) {
                    String menuImagePath = saveToDisk(menuImage, sessionUser.getId());
                    menuImagePaths.add(menuImagePath);
                }
            }
            kitchenRequest.setMenuImagePaths(menuImagePaths);

            // Save other documents (FSSAI, PAN) if needed
            if (!fssaiDocument.isEmpty()) {
                String fssaiDocPath = saveToDisk(fssaiDocument, sessionUser.getId());
                kitchenRequest.setFssaiDocumentPath(fssaiDocPath);
            }

            if (!panDocument.isEmpty()) {
                String panDocPath = saveToDisk(panDocument, sessionUser.getId());
                kitchenRequest.setPanDocumentPath(panDocPath);
            }

            // Save or update the KitchenRequest in the database
            kitchenService.saveKitchenRequest(kitchenRequest);

            return "redirect:/kitchens/kitchen-registration";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to submit kitchen request. Please try again.");
            return "redirect:/kitchens/kitchen-registration"; // Redirect back to the form on failure
        }
    }


    @PostMapping("/statusUpdate")
    @ResponseBody
    public Map<String, Object> updateKitchenStatus(@RequestBody KitchenStatusUpdateRequest kitchenRequest, HttpSession session) {
        boolean updated = kitchenService.updateKitchenStatus(kitchenRequest.getKitchenId(), kitchenRequest.getIsApproved());
        Map<String, Object> response = new HashMap<>();

        if(updated){
            response.put("status", "success");
            response.put("message", "kitchen/user/chef updated successfully.");
        }else{
            response.put("status", "failed");
            response.put("message", "failed to update");
        }
        return response;
    }

    @PostMapping("/getKitchenDocuments")
    public ResponseEntity<Map<String, Object>> getKitchenDocuments(@RequestBody Map<String, Object> request) {
        // Extract kitchenId as Integer
        Integer kitchenIdInteger = (Integer) request.get("kitchenId");

        // Convert Integer to Long
        Long kitchenId = kitchenIdInteger != null ? Long.valueOf(kitchenIdInteger) : null;

        Kitchen kitchen = kitchenService.getKitchenById(kitchenId);
        Map<String, Object> documents = new HashMap<>();

        if(kitchen != null){
            documents.put("kitchenImagePath", kitchen.getKitchenImagePath().replace("\\", "/"));
            documents.put("menuImagePaths", kitchen.getMenuImagePaths());
            documents.put("fssaiDocumentPath", kitchen.getFssaiDocumentPath().replace("\\", "/"));
            documents.put("panDocumentPath", kitchen.getPanDocumentPath().replace("\\", "/"));
        }

        // Prepare the response
        if (documents != null && !documents.isEmpty()) {
            return ResponseEntity.ok(Map.of("status", "success", "documents", documents));
        } else {
            return ResponseEntity.ok(Map.of("status", "failure", "message", "No documents found"));
        }
    }

    // Helper method to save images to disk
    private String saveToDisk(MultipartFile file, Long userId) throws IOException {

        // Ensure kitchenId is valid
        if (file.isEmpty() || userId == null) {
            throw new IllegalArgumentException("Invalid file or kitchenId");
        }

        String kitchenFolder = UPLOAD_DIR + userId + "/";

        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path path = Paths.get(kitchenFolder + fileName);
        
        // Ensure the directory exists, otherwise create it
        Files.createDirectories(path.getParent());
        
        Files.write(path, file.getBytes());
        String returnPath = "/uploads/kitchenRequest/" + userId + "/" + fileName;
        return returnPath; // Return the file path
    }

    // Helper method to check kitchen status
    private Boolean checkKitchenStatus(User sessionUser) {
        
        Kitchen kitchen = kitchenService.findKitchenByUser(sessionUser);
        
        boolean underVerification = kitchen != null && kitchen.getStatus() == KitchenStatus.UNDER_VERIFICATION;
        
        return underVerification;
    }
}


