package com.eneskaydi.yazilimprojeyonetimi.Repository;

import com.eneskaydi.yazilimprojeyonetimi.Entity.Document;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Property;
import com.eneskaydi.yazilimprojeyonetimi.Entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Belirli bir mülke ait tüm belgeleri bulma.
    List<Document> findByProperty(Property property);

    // Belirli bir sahibe ait tüm belgeleri bulma.
    List<Document> findByOwner(Owner owner);

    // Belirli bir mülke VE belirli bir sahibe ait belgeleri bulma
    // (Eğer bir belge hem mülke hem de sahibe bağlı olabiliyorsa).
    List<Document> findByPropertyAndOwner(Property property, Owner owner);

    // Dosya adına göre arama (kısmi eşleşme ile).
    List<Document> findByFileNameContainingIgnoreCase(String fileName);
}