package com.dh.digitalbooking.service.imp;

import com.dh.digitalbooking.dto.policyType.PolicyTypeFullDTO;
import com.dh.digitalbooking.dto.policyType.PolicyTypeNoIdDTO;
import com.dh.digitalbooking.exception.BadRequestException;
import com.dh.digitalbooking.exception.NotFoundException;
import com.dh.digitalbooking.entity.PolicyType;
import com.dh.digitalbooking.mapper.PolicyTypeMapper;
import com.dh.digitalbooking.repository.PolicyTypeRepository;
import com.dh.digitalbooking.service.PolicyTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PolicyTypeServiceImp implements PolicyTypeService {
    private final PolicyTypeRepository policyTypeRepository;
    private final PolicyTypeMapper policyTypeMapper;

    public PolicyTypeServiceImp(PolicyTypeRepository policyTypeRepository, PolicyTypeMapper policyTypeMapper) {
        this.policyTypeRepository = policyTypeRepository;
        this.policyTypeMapper = policyTypeMapper;
    }

    @Override
    public List<PolicyTypeFullDTO> allPoliciesTypes() {
        return policyTypeRepository.findAll().stream().map(policyTypeMapper::policyTypeToPolicyTypeFullDTO).toList();
    }

    @Override
    public PolicyTypeFullDTO getPolicieTypeById(Long id) {
        return policyTypeMapper.policyTypeToPolicyTypeFullDTO(existByIdValidation(id));
    }

    @Override
    @Transactional
    public PolicyTypeFullDTO savePolicyType(PolicyTypeNoIdDTO policyTypeNoIdDTO) {
        String name = policyTypeNoIdDTO.name();

        if(policyTypeRepository.findByName(name).isPresent())
            throw new BadRequestException("There is already a policy with the name '" + name + "'");
        PolicyType policyType = policyTypeMapper.policyTypeNoIdToPolicyType(policyTypeNoIdDTO);

        return policyTypeMapper.policyTypeToPolicyTypeFullDTO(policyTypeRepository.save(policyType));
    }

//    BORRAR RELACION BIDIRECCIONAL
    @Override
    @Transactional
    public void deletePolicyType(Long id) {
        PolicyType policyType = existByIdValidation(id);
        if(!(policyType.getPoliticas().isEmpty()))
            throw new BadRequestException("You cannot delete the policy with id " + id + " because there are policies registered with this type");
        policyTypeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PolicyTypeFullDTO updatePolicyType(PolicyTypeFullDTO policyTypeFullDTO) {
        Long id = policyTypeFullDTO.id();
        PolicyType policyType = existByIdValidation(id);
        String name = policyTypeFullDTO.name();

        PolicyType policyTypeByNombre = policyTypeRepository.findByName(name).orElse(null);
        if(policyTypeByNombre != null && !(policyTypeByNombre.getId().equals(id)))
            throw new BadRequestException("There is already a policy with the name '" + name + "'");

        policyType.setName(name);
        return policyTypeFullDTO;
    }

    @Override
    public PolicyType existByIdValidation(Long id) {
        return policyTypeRepository.findById(id).orElseThrow(() ->
                new NotFoundException("PolicyType with " + id + " not found"));
    }
}