// Service Worker pour Firebase Cloud Messaging

importScripts('https://www.gstatic.com/firebasejs/10.7.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.7.1/firebase-messaging-compat.js');

// ⚠️ REMPLACEZ PAR VOTRE CONFIGURATION
const firebaseConfig = {
    apiKey: "AIzaSyA0-rt-JzOFy5Gqa-6-RV50ZXxIl62aPEI",
    authDomain: "notification-push-test-tekup.firebaseapp.com",
    projectId: "notification-push-test-tekup",
    storageBucket: "notification-push-test-tekup.firebasestorage.app",
    messagingSenderId: "112186616489",
    appId: "1:112186616489:web:7547d31c6761ec7ab957b8",
    measurementId: "G-2MF8TXBF32"
};

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

// Gérer les notifications en arrière-plan
messaging.onBackgroundMessage((payload) => {
    console.log('Notification en arrière-plan reçue:', payload);

    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: 'https://img.icons8.com/color/96/000000/notification.png'
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});
