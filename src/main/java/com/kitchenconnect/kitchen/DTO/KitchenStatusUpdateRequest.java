package com.kitchenconnect.kitchen.DTO;

import com.kitchenconnect.kitchen.enums.KitchenStatus;

public class KitchenStatusUpdateRequest {
    private Long kitchenId;
    private KitchenStatus status;

    public KitchenStatus getStatus() {
        return status;
    }

    public void setStatus(KitchenStatus status) {
        this.status = status;
    }

    public Long getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }

}
