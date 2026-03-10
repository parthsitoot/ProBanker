import React from 'react';

const AuthFrame = ({ eyebrow, title, description, children, footer }) => {
  return (
    <div className="auth-shell">
      <section className="auth-hero">
        <p className="auth-hero-kicker">proBanker</p>
        <h1>Banking that feels controlled, not cluttered.</h1>
        <p className="auth-hero-copy">
          A focused customer console for authentication, account access, transfers, statements, and profile management.
        </p>

        <div className="auth-hero-grid">
          <div className="auth-hero-card">
            <span>OTP login</span>
            <strong>Multi-step sign in already wired to your backend</strong>
          </div>
          <div className="auth-hero-card">
            <span>Transaction security</span>
            <strong>Deposit, withdrawal, transfer, and PIN setup in one flow</strong>
          </div>
        </div>
      </section>

      <section className="auth-panel">
        <div className="auth-panel-head">
          <p className="auth-eyebrow">{eyebrow}</p>
          <h2>{title}</h2>
          <p className="auth-description">{description}</p>
        </div>

        {children}
        {footer ? <div className="auth-footer">{footer}</div> : null}
      </section>
    </div>
  );
};

export default AuthFrame;
