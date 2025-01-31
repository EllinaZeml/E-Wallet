package ru.cft.template.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.api.dto.WalletDTO;
import ru.cft.template.core.entity.WalletEntity;
import ru.cft.template.core.service.WalletService;

@RestController
@RequestMapping(name = "/wallets")
public class WalletController {

    private final WalletService walletService;
    @Autowired
    public WalletController(WalletService walletService){
        this.walletService=walletService;
    }

    @GetMapping("/{userId}")
    public WalletDTO getWallet(@PathVariable Long userId){
       WalletEntity wallet = walletService.getWallet(userId);
       return new WalletDTO(wallet.getUser().getId(),wallet.getWalletNumber(), wallet.getBalance());
    }

    @PostMapping("/{userId}/hesoyam")
    public WalletDTO performHesoyam(@PathVariable Long userId){
        WalletEntity wallet = walletService.performHESOYAM(userId);
        return new WalletDTO(wallet.getUser().getId(),wallet.getWalletNumber(), wallet.getBalance());
    }



}
