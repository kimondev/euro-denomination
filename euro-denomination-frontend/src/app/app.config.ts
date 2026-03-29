import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import Nora from "@primeuix/themes/nora";

import { routes } from './app.routes';
import {providePrimeNG} from 'primeng/config';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),

    providePrimeNG({
      theme: {
        preset: Nora,
        options: {
          darkModeSelector: ".dark-mode",
        },
      },
    }),
  ]
};
