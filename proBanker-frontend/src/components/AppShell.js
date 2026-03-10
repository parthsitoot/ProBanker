import React from 'react';
import { NavLink, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const navigationItems = [
  { to: '/dashboard', label: 'Overview' },
  { to: '/deposit', label: 'Deposit' },
  { to: '/withdraw', label: 'Withdraw' },
  { to: '/transfer', label: 'Transfer' },
  { to: '/statement', label: 'Statement' },
  { to: '/profile', label: 'Profile' },
  { to: '/create-pin', label: 'PIN' },
];

const AppShell = ({ title, subtitle, actions, children }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { logout, user } = useAuth();

  const initials = `${user?.firstName?.[0] || 'P'}${user?.lastName?.[0] || 'B'}`.toUpperCase();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <div className="app-shell">
      <aside className="app-sidebar">
        <div className="app-brand">
          <div className="app-brand-mark">PB</div>
          <div>
            <p className="app-brand-name">proBanker</p>
            <p className="app-brand-copy">Retail banking console</p>
          </div>
        </div>

        <nav className="app-nav" aria-label="Primary">
          {navigationItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `app-nav-link${isActive ? ' active' : ''}${location.pathname === item.to ? ' current' : ''}`
              }
            >
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div className="app-sidebar-foot">
          <p>Protected by OTP login and transaction PIN flows.</p>
        </div>
      </aside>

      <div className="app-main">
        <header className="app-header">
          <div>
            <p className="page-eyebrow">Banking workspace</p>
            <h1>{title}</h1>
            {subtitle ? <p className="page-subtitle">{subtitle}</p> : null}
          </div>

          <div className="app-header-tools">
            {actions}
            <div className="app-user-chip">
              <div className="app-user-avatar">{initials}</div>
              <div>
                <p>{user ? `${user.firstName || ''} ${user.lastName || ''}`.trim() || 'Account user' : 'Account user'}</p>
                <button type="button" className="text-action" onClick={handleLogout}>
                  Sign out
                </button>
              </div>
            </div>
          </div>
        </header>

        <main className="page-content">{children}</main>
      </div>
    </div>
  );
};

export default AppShell;
