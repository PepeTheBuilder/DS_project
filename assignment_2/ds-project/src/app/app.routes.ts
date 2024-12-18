import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { AdminpageComponent } from './adminpage/adminpage.component';
import { UserDevicesComponent } from './userpage/userpage.component';
import { UserConsumtionComponent } from './user-consumtion/user-consumtion.component';
import { DevicechartComponent } from './devicechart/devicechart.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent
    },
    {
        path: 'login',
        component: LoginFormComponent
    },
    {
        path: 'admin',
        component: AdminpageComponent
    },
    {
        path: 'user',
        component: UserDevicesComponent
    },
    {
        path: 'userConsumtion',
        component: UserConsumtionComponent
    },
    {
        path: 'devicechart/:id',
        component: DevicechartComponent
    }
];
