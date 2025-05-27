const payload = {
  email: email,
  password: password,
};

// const response = await fetch('http://localhost:8080/api/auth/login', { // Eski direkt URL
const response = await fetch('/api/auth/login', { // Yeniden yazma kuralını kullanmak için göreceli URL
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(payload),
}); 