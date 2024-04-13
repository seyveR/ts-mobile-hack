from . import views
from django.urls import path
from .views import  AddRecord

urlpatterns = [
    path('', views.tickets, name = 'tickets'),
    path('c_tickets/', views.c_tickets, name='c_tickets'),
    path('add_record/', AddRecord.as_view()),
]
