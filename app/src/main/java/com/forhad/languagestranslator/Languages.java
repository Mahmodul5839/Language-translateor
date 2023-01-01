package com.forhad.languagestranslator;

import com.mannan.translateapi.Language;

public class Languages {
    public static String[] langCodesEN = {Language.AFRIKAANS, Language.ALBANIAN, Language.ARABIC, Language.ARMENIAN, Language.AZERBAIJANI, Language.BASQUE, Language.BELARUSIAN, Language.BENGALI, Language.BULGARIAN, Language.CATALAN, "zh-CN", Language.CROATIAN, Language.CZECH, Language.DANISH, Language.DUTCH, Language.ENGLISH, Language.ESTONIAN, Language.FILIPINO, Language.FINNISH, Language.FRENCH, Language.GALICIAN, Language.GEORGIAN, Language.GERMAN, Language.GREEK, Language.GUJARATI, Language.HAITIAN_CREOLE, Language.HEBREW, Language.HINDI, Language.HUNGARIAN, Language.ICELANDIC, Language.INDONESIAN, Language.IRISH, Language.ITALIAN, Language.JAPANESE, Language.KANNADA, Language.KOREAN, Language.LATIN, Language.LATVIAN, Language.LITHUANIAN, Language.MACEDONIAN, Language.MALAY, Language.MALTESE, Language.NORWEGIAN, Language.PERSIAN, Language.POLISH, Language.PORTUGUESE, Language.ROMANIAN, Language.RUSSIAN, Language.SERBIAN, Language.SLOVAK, Language.SLOVENIAN, Language.SPANISH, Language.SWAHILI, Language.SWEDISH, Language.TAMIL, Language.TELUGU, Language.THAI, Language.TURKISH, Language.UKRAINIAN, Language.URDU, Language.VIETNAMESE, Language.WELSH, Language.YIDDISH};
    public static String[] langCodesTTS = {Language.AFRIKAANS, "sq-AL", "ar-AE", "hy-AM", "az-AZ", "eu-ES", "en-US", "en-US", "bg-BG", "ca-ES", "zh", "hr-BA", "cs-CZ", "da-DK", "nl-BE", "en-US", "et-EE", "", "fi-FI", "fr-CA", "gl-ES", "ka-GE", "de-DE", "el-GR", "gu-IN", "en-US", "he-IL", "hi-IN", "hu-HU", "is-IS", "id-ID", "en-US", "it-IT", "ja-JP", "kn-IN", "ko-KR", "sr-BA", "lv-LV", "lt-LT", "mk-MK", "ms-MY", "mt-MT", "nb-NO", "ur-PK", "pl-PL", "pt-PT", "ro-RO", "ru-RU", "sr-BA", "sk-SK", "sl-SI", "es-AR", "sw-KE", "en-US", "ta-IN", "te-IN", "th-TH", "tr-TR", "uk-UA", "ur-PK", "vi-VN", "cy-GB", "de-AT"};
    public static String[] langsEN = {"Afrikaans", "Albanian", "Arabic", "Armenian", "Azerbaijan", "Basque", "Belarussian", "Bengali", "Bulgarian", "Catalan", "Chinese", "Croatian", "Czech", "Danish", "Dutch", "English", "Estonian", "Filipino", "Finnish", "French", "Galician", "Georgian", "German", "Greek", "Gujarati", "Haitian", "Hebrew", "Hindi", "Hungarian", "Icelandic", "Indonesian", "Irish", "Italian", "Japanese", "Kannada", "Korean", "Latin", "Latvian", "Lithuanian", "Macedonian", "Malay", "Maltese", "Norwegian", "Persian", "Polish", "Portuguese", "Romanian", "Russian", "Serbian", "Slovak", "Slovenian", "Spanish", "Swahili", "Swedish", "Tamil", "Telugu", "Thai", "Turkish", "Ukrainian", "Urdu", "Vietnamese", "Welsh", "Yiddish"};

    public static String[] getLangCodesTTS() {
        return langCodesTTS;
    }

    public static String[] getLangsEN() {
        return langsEN;
    }

    public static String getLangCodeEN(int i) {
        return langCodesEN[i];
    }
}
