# MOBE-s-runner
Mini jeu android pour l'UE MOBE du Master SDL - Université Paul Sabatier

## **Equipe**
BAYCHELIER Rémi<br>
LACROUX Nathan<br>
PEYRICHOUX Rémy<br>
TEYTAU Emilie

## **Lien dépot**
https://github.com/Emyau/MOBE-s-runner

## **Présentation**
Notre jeu est l'histoire d'un petit poussin condamné à courrir pour l'éternité. 

Il se balade au fil du niveau sans fin et doit se mesurer à différents obstacles...

Ou MOURIR.

### **Capteurs utilisés**
Nous avons choisi d'utiliser les trois capteurs suivants :

- Le **microphone** (lorsqu'on souffle assez fort dessus, et donc qu'il détecte un volume sonore important, les obstacles de type feu disparaissent)
- L'**accéléromètre** (lorsqu'on secoue le téléphone, les obstacles de type rochers disparaissent)
- Le **gyroscope** (lorsqu'on incline le téléphone, les obstacles de type gate s'abaissent)

Plus vous restez en vie, plus votre score augmente. Le meilleur score est enregistré.

## **Installation**
### **Via APK pré-compilé**
Un APK déjà compilé est disponible dans la page release du dépot. Pour le lancer :
- Le télécharger et le transférer sur un appareil
- Lancer l'APK qui installera le jeu

### **Compilation d'APK**
- Importer le projet sur Android Studio (soit via git soit en téléchargeant l'archive du projet)
- Lancer un build du projet
- Importer l'APK généré dans app/build/generated/outputs/apk sur l'appareil
- Lancer l'APK qui installera le jeu

### **Via débug Android Studio**
- Importer le projet sur Android Studio (soit via git soit en téléchargeant l'archive du projet)
- Lancer un build du projet
- Lancer app sur un appareil avec le mode développeur activé

## **Licence des assets**
Tous les assets de l'application ont soit été produits par l'équipe, soit importés et/ou modifiés depuis ce site :
https://opengameart.org/

Les assets de ce site sont libres d'utilisation : https://opengameart.org/content/faq#q-proprietary
