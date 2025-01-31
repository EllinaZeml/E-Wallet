package ru.cft.template.core.service;

import ru.cft.template.core.entity.WalletEntity;


public interface WalletService {
    WalletEntity getWallet(Long id);
    WalletEntity performHESOYAM(Long userId);
}
