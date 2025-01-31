package ru.cft.template.api.dto;

public record WalletDTO(
        Long userId,
        int walletNumber,
        int balance
){
}
