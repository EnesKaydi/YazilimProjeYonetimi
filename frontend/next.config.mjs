/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost:8080/api/:path*', // Backend API adresiniz
      },
    ];
  },
  allowedDevOrigins: ["http://localhost:3000", "http://10.203.81.156:3000"], // Direkt config altına alıyoruz
};

export default nextConfig;
