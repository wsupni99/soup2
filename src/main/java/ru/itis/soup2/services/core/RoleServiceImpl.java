package ru.itis.soup2.services.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.soup2.models.core.Role;
import ru.itis.soup2.repositories.core.RoleRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String roleName) {
        try {
            return roleRepository.findByRoleName(roleName);
        } catch (Exception e) {
            log.error("Ошибка при поиске роли по имени: {}", roleName, e);
            throw e;
        }
    }

    @Override
    public List<Role> findAll() {
        try {
            return roleRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении списка ролей", e);
            throw e;
        }
    }
}