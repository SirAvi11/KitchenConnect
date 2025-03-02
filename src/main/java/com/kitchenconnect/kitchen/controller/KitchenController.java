package com.kitchenconnect.kitchen.controller;

import com.kitchenconnect.kitchen.entity.FoodItem;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.KitchenRequest;
import com.kitchenconnect.kitchen.service.FoodItemService;
import com.kitchenconnect.kitchen.service.KitchenService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/kitchens")
public class KitchenController {
    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private FoodItemService foodItemService;

    private static final String IMAGE_DIR = "/uploads/directory/";

    @GetMapping
    public String showAllKitchens(Model model) {
        List<Kitchen> allKitchens = kitchenService.getAllKitchens();

        model.addAttribute("kitchens", allKitchens);
        return "kitchens";
    }

    @GetMapping("/{id}")
    public String showKitchenDetails(@PathVariable Long id, Model model, HttpSession session) {
        Kitchen kitchen = kitchenService.getKitchenById(id);

        if (kitchen != null) {
            // Fetch the chef's name from the associated Chef entity
            String chefName = kitchen.getChef().getUser().getFirstname() + " " + kitchen.getChef().getUser().getLastname();
            Long chefId = kitchen.getChef().getChefId();

            //Fetch Food items (menu)
            List<FoodItem> menuItems = foodItemService.findByKitchenId(id);

            // Retrieve cart from session
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }

            model.addAttribute("kitchen", kitchen);
            model.addAttribute("chefName", chefName); // Add chef's name to the model
            model.addAttribute("chefId", chefId); // Add chef's Id to the model
            model.addAttribute("menuItems", menuItems); // Add menu items to model
            model.addAttribute("cartItems", cart);

            return "kitchenpage";
        }
        
        return "redirect:/"; // Redirect to homepage if kitchen not found
    }

    @PostMapping("/submitKitchenForm")
    public String saveKitchenRequest(@ModelAttribute KitchenRequest kitchenRequest,
                                    @RequestParam("menuImages") List<MultipartFile> menuImages
                                    /* @RequestParam("kitchenImage") MultipartFile kitchenImage,
                                    // @RequestParam("fssaiDocument") MultipartFile fssaiDocument,
                                    // @RequestParam("panDocument") MultipartFile panDocument,
                                    // @RequestParam("selectedCuisines") String selectedCuisines,
                                    // @RequestParam("openDays") List<String> openDays*/) throws IOException {

        // Process the cuisines as a comma-separated string
        // kitchenRequest.setSelectedCuisines(Arrays.asList(selectedCuisines.split(",")));
        // kitchenRequest.setOpenDays(openDays);

        // Save kitchen image and set its path
        // if (!kitchenImage.isEmpty()) {
        //     String kitchenImagePath = saveImageToDisk(kitchenImage);
        //     kitchenRequest.setKitchenImagePath(kitchenImagePath);  // Ensure this method exists
        // }

        // Save menu images and set their paths
        List<String> menuImagePaths = new ArrayList<>();
        for (MultipartFile menuImage : menuImages) {
            if (!menuImage.isEmpty()) {
                String menuImagePath = saveImageToDisk(menuImage);
                menuImagePaths.add(menuImagePath);
            }
        }
        kitchenRequest.setMenuImagePaths(menuImagePaths);

        // Save other documents (FSSAI, PAN) if needed
        // if (!fssaiDocument.isEmpty()) {
        //     String fssaiDocPath = saveFileToDisk(fssaiDocument);
        //     kitchenRequest.setFssaiDocument(fssaiDocPath);  // Ensure this method exists
        // }

        // if (!panDocument.isEmpty()) {
        //     String panDocPath = saveFileToDisk(panDocument);
        //     kitchenRequest.setPanDocument(panDocPath);  // Ensure this method exists
        // }

        // Save the KitchenRequest in the database
        //kitchenService.saveKitchenRequest(kitchenRequest);

        //return "redirect:/kitchens/success"; // Redirect to a success page
        return "redirect:/";
    }

    // Helper method to save images to disk
    private String saveImageToDisk(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path path = Paths.get(IMAGE_DIR + fileName);
        
        // Ensure the directory exists, otherwise create it
        Files.createDirectories(path.getParent());
        
        Files.write(path, file.getBytes());
        return path.toString(); // Return the file path
    }

    // Helper method to save documents to disk
    private String saveFileToDisk(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path path = Paths.get(IMAGE_DIR + fileName);

        // Ensure the directory exists, otherwise create it
        Files.createDirectories(path.getParent());
        
        Files.write(path, file.getBytes());
        return path.toString(); // Return the file path
    }
}


