import 'bootstrap/dist/css/bootstrap.min.css';
import { AuthProvider } from './context/AuthContext';
import { LanguageProvider } from './context/LanguageContext';
import BootstrapClient from './components/BootstrapClient';
import LanguageWrapper from './components/LanguageWrapper';

export const metadata = {
  title: 'Association Management',
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <LanguageProvider>
          <LanguageWrapper>
            <AuthProvider>
              <BootstrapClient />
              {children}
            </AuthProvider>
          </LanguageWrapper>
        </LanguageProvider>
      </body>
    </html>
  );
}