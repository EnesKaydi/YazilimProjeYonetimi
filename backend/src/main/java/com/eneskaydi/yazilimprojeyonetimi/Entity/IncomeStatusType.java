package com.eneskaydi.yazilimprojeyonetimi.Entity;

// Gelir kaydının durumunu belirten enum.
// Database_Prd.md [cite: 23, 24, 25, 27] gereksinimlerine göre.
// PRD'deki tanımlamalar: "Henüz Gelmedi", "Ödendi", "Ödenmedi", "Kısmen Ödenmiş"
public enum IncomeStatusType {
    HENUZ_GELMEDI, // Henüz vadesi gelmemiş veya gelecek aylar için (PRD [cite: 23])
    ODENDI, // Tamamı ödendi (PRD [cite: 24])
    ODENMEDI, // Ödenmedi (PRD [cite: 25])
    KISMEN_ODENMIS // Kısmen ödendi (PRD [cite: 27])
}