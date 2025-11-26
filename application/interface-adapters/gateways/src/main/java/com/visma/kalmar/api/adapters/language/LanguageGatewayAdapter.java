package com.visma.kalmar.api.adapters.language;

import com.visma.kalmar.api.entities.language.Language;
import com.visma.kalmar.api.exception.ResourceNotFoundException;
import com.visma.kalmar.api.language.LanguageGateway;
import com.visma.feature.kalmar.api.language.LanguageRepository;

import java.util.UUID;

public class LanguageGatewayAdapter implements LanguageGateway {

    private final LanguageRepository languageRepository;

    public LanguageGatewayAdapter(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public Language findByCode(String code) {
        return languageRepository.findByCode(code)
                .map(lang -> new Language(lang.getIdLanguage(), lang.getName(), lang.getCode()))
                .orElseThrow(() -> new ResourceNotFoundException("Language", "Language not found with code: " + code));
    }

    @Override
    public Language findById(UUID idLanguage) {
        return languageRepository.findById(idLanguage)
                .map(lang -> new Language(lang.getIdLanguage(), lang.getName(), lang.getCode()))
                .orElseThrow(() -> new ResourceNotFoundException("Language", "Language not found with id: " + idLanguage));
    }
}
