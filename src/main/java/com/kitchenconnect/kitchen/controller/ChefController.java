package com.kitchenconnect.kitchen.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.kitchenconnect.kitchen.DTO.ChefRequest;
import com.kitchenconnect.kitchen.entity.Chef;
import com.kitchenconnect.kitchen.service.ChefService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/chef")
public class ChefController {
    private ChefService chefService;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/chef/";


    public ChefController(ChefService chefService){
        super();
        this.chefService = chefService;
    }

    @GetMapping("/{id}")
    public String showChefPage(@PathVariable Long id, Model model) {
        Chef chef = chefService.getChefById(id);

        if (chef != null) {
            model.addAttribute("chef", chef);
            return "chef";
        }
        
        return "redirect:/"; // Redirect to homepage if kitchen not found
    }

    @PostMapping("/update-biography")
    public String updateChefBiography(@ModelAttribute("chefDetails") Chef updatedChef) {
        // Fetch the existing chef from the database
        Chef existingChef = chefService.getChefById(updatedChef.getChefId());

        // Update only the allowed fields (e.g., biography)
        existingChef.setBiography(updatedChef.getBiography());

        // Save the updated chef
        chefService.updateChef(existingChef);

        return "redirect:/dashboard"; // Redirect back to the dashboard
    }
    
    @PostMapping("/upload-profile-image")
    public ResponseEntity<Map<String, Object>> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("chefId") Long chefId) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Save the file to disk and get the file path
            String imagePath = saveToDisk(file, chefId);

            // Save the file path in the database
            Chef chef = chefService.getChefById(chefId);
            chef.setChefProfilePicture(imagePath);
            chefService.updateChef(chef);

            // Return the new image path
            response.put("success", true);
            response.put("imagePath", imagePath);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Failed to upload image.");
        }

        return ResponseEntity.ok(response);
    }

    private String saveToDisk(MultipartFile file, Long chefId) throws IOException {

        // Ensure userId is valid
        if (file.isEmpty() || chefId == null) {
            throw new IllegalArgumentException("Invalid file or chefId");
        }

        String menuFolder = UPLOAD_DIR + chefId + "/";

        String fileName = "profile_picture" + "-" + file.getOriginalFilename();

        Path path = Paths.get(menuFolder + fileName);
        
        // Ensure the directory exists, otherwise create it
        Files.createDirectories(path.getParent());
        
        Files.write(path, file.getBytes());
        String returnPath = "/uploads/chef/" + chefId + "/" + fileName;
        return returnPath; // Return the file path
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<ChefRequest> getChefDetails(@PathVariable Long userId) {
        Chef chef = chefService.getChefByUserId(userId);
        if (chef == null) {
            return ResponseEntity.notFound().build();
        }
    
        ChefRequest dto = new ChefRequest(
            chef.getBiography(),
            chef.getFavouriteDishes(),
            chef.getChefProfilePicture()
        );
    
        return ResponseEntity.ok(dto);
    }
    

}
