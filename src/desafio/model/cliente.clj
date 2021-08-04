(ns desafio.model.cliente
  (:require [schema.core :as s]))

(def Cliente
  { :cliente-id s/Uuid
    :nome s/Str
   :cpf s/Str
   :email s/Str
   })


(s/defn cria-novo-cliente :- Cliente
  [nome :- s/Str
   cpf :- s/Str
   email :- s/Str]
  {:cliente-id (str (java.util.UUID/randomUUID))
   :nome nome
   :cpf cpf
   :email email})

